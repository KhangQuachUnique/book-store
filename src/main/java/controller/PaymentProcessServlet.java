package controller;

import dao.AddressDao;
import dao.PromotionDAO;
import model.*;
import service.CartService;
import service.MoMoService;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import constant.PathConstants;

/**
 * Process payment - Handle payment method selection and initiate payment
 */
@WebServlet("/user/checkout/process")
public class PaymentProcessServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(PaymentProcessServlet.class.getName());

    private final CartService cartService = new CartService();
    private final MoMoService momoService = new MoMoService();
    private final OrderService orderService = new OrderService();
    private final AddressDao addressDao = new AddressDao();
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            // Get payment method
            String paymentMethod = req.getParameter("paymentMethod");
            String addressIdStr = req.getParameter("addressId");
            String notes = req.getParameter("notes");
            String promoCode = req.getParameter("promoCode");

            // Validate
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                req.setAttribute("error", "Vui lòng chọn phương thức thanh toán");
                req.getRequestDispatcher("/user/payment").forward(req, resp);
                return;
            }

            if (addressIdStr == null || addressIdStr.isEmpty()) {
                req.setAttribute("error", "Vui lòng chọn địa chỉ giao hàng");
                req.getRequestDispatcher("/user/payment").forward(req, resp);
                return;
            }

            // Get cart
            List<CartItem> cart = cartService.getItems(user.getId());
            if (cart == null || cart.isEmpty()) {
                req.setAttribute("error", "Giỏ hàng trống");
                resp.sendRedirect(req.getContextPath() + "/user/cart");
                return;
            }

            // Parse address ID
            long addressId = Long.parseLong(addressIdStr);

            // Get address
            Address address = addressDao.findByIdAndUserId(addressId, user.getId());
            if (address == null) {
                req.setAttribute("error", "Địa chỉ không hợp lệ");
                req.getRequestDispatcher("/user/payment").forward(req, resp);
                return;
            }

            // Get promotion if provided
            Promotion promotion = null;
            if (promoCode != null && !promoCode.trim().isEmpty()) {
                promotion = promotionDAO.getPromotionByCode(promoCode.trim());
                if (promotion == null || !promotion.isValid()) {
                    req.setAttribute("error", "Mã khuyến mãi không hợp lệ hoặc đã hết hạn");
                    req.getRequestDispatcher("/user/payment").forward(req, resp);
                    return;
                }
            }

            // Calculate total
            double subtotal = cartService.calculateSubtotal(cart);
            double shippingFee = cartService.getShippingFee();
            double discountAmount = 0.0;
            
            if (promotion != null) {
                discountAmount = (subtotal + shippingFee) * promotion.getDiscount() / 100.0;
            }
            
            double total = subtotal + shippingFee - discountAmount;

            // Store order info in session for MoMo callback
            session.setAttribute("pendingOrderTotal", total);
            session.setAttribute("pendingOrderAddressId", addressId);
            session.setAttribute("pendingOrderNotes", notes);
            session.setAttribute("pendingOrderPaymentMethod", paymentMethod);
            session.setAttribute("pendingOrderPromoCode", promoCode);

            // Handle payment based on method
            if ("momo".equalsIgnoreCase(paymentMethod)) {
                handleMoMoPayment(req, resp, user, total, cart, address, promotion, notes);
                return;
            } else {
                // COD - create order directly
                handleCODPayment(req, resp, user, cart, address, promotion, notes, total);
                return;
            }

        } catch (Exception e) {
            log.severe("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra khi xử lý thanh toán");
            req.getRequestDispatcher("/user/payment").forward(req, resp);
        }
    }

    /**
     * Handle MoMo payment
     */
    private void handleMoMoPayment(HttpServletRequest req, HttpServletResponse resp,
                                   User user, double total, List<CartItem> cart,
                                   Address address, Promotion promotion, String notes) 
            throws IOException, ServletException {
        try {
            // Generate order ID
            String orderId = "ORDER_" + System.currentTimeMillis() + "_" + user.getId();

            // Store cart items and order details in session for later order creation
            req.getSession().setAttribute("pendingMoMoOrderId", orderId);
            req.getSession().setAttribute("pendingMoMoCart", cart);
            req.getSession().setAttribute("pendingMoMoAddress", address);
            req.getSession().setAttribute("pendingMoMoPromotion", promotion);
            req.getSession().setAttribute("pendingMoMoNotes", notes);

            // Convert to long (VND - no decimal)
            long amount = (long) total;
            String orderInfo = "Thanh toán đơn hàng BookieCake - " + orderId;

            String baseUrl = buildBaseUrl(req);
            String redirectUrl = baseUrl + "/payment/momo/callback";
            String ipnUrl = baseUrl + "/payment/momo/ipn";
            String requestId = "RID" + System.currentTimeMillis();
            String extraData = "userId=" + user.getId();

            // Create MoMo payment
            MoMoPaymentResponse momoResponse = momoService.createPayment(orderId, requestId, amount, orderInfo, redirectUrl, ipnUrl, extraData);

            // Check if successful
            if (momoResponse != null && momoResponse.getResultCode() == 0 && momoResponse.getPayUrl() != null) {
                // Redirect to MoMo payment page
                resp.sendRedirect(momoResponse.getPayUrl());
            } else {
                String errorMsg = momoResponse != null ? momoResponse.getMessage() : "Không thể kết nối MoMo";
                req.setAttribute("error", "Lỗi MoMo: " + errorMsg);
                req.getRequestDispatcher("/user/payment").forward(req, resp);
            }

        } catch (Exception e) {
            log.severe("MoMo payment error: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Không thể khởi tạo thanh toán MoMo");
            req.getRequestDispatcher("/user/payment").forward(req, resp);
        }
    }

    /**
     * Handle COD payment - create order immediately
     */
    private void handleCODPayment(HttpServletRequest req, HttpServletResponse resp,
                                  User user, List<CartItem> cart, Address address,
                                  Promotion promotion, String notes, double total)
            throws ServletException, IOException {
        try {
            // Create order object
            Order order = new Order();
            order.setUser(user);
            order.setAddress(address);
            order.setPromotion(promotion);
            order.setPaymentMethod("CASH_ON_DELIVERY");
            order.setStatus(OrderStatus.PROCESSING);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(total);

            // Create order items from cart
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart) {
                OrderItem orderItem = new OrderItem();
                orderItem.setBook(cartItem.getBook());
                orderItem.setQuantity(cartItem.getQuantity());
                // Price will be set in OrderDAO.createOrder()
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
            order.setItems(orderItems);

            // Save order to database
            orderService.createOrder(order);

            // Gửi email xác nhận đơn hàng
            try {
                util.SendMailUtil.sendOrderConfirmationEmail(
                    user.getEmail(),
                    user.getName(),
                    String.valueOf(order.getId()),
                    String.format("%.0f", order.getTotalAmount())
                );
            } catch (Exception mailEx) {
                log.warning("Không thể gửi email xác nhận đơn hàng: " + mailEx.getMessage());
            }

            // Clear cart after successful order
            cartService.clearCart(user.getId());

            // Clear session data
            req.getSession().removeAttribute("pendingOrderTotal");
            req.getSession().removeAttribute("pendingOrderAddressId");
            req.getSession().removeAttribute("pendingOrderNotes");
            req.getSession().removeAttribute("pendingOrderPaymentMethod");
            req.getSession().removeAttribute("pendingOrderPromoCode");

            // Forward to themed order success page
            req.setAttribute("orderId", order.getId());
            req.setAttribute("orderTotal", order.getTotalAmount());
            req.setAttribute("paymentMethod", "COD");
            req.setAttribute("success", "Đặt hàng thành công! Đơn hàng đang được xử lý.");
            req.setAttribute("contentPage", "/WEB-INF/views/order-success.jsp");
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);

        } catch (Exception e) {
            log.severe("Error creating COD order: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            req.getRequestDispatcher("/user/payment").forward(req, resp);
        }
    }

    private String buildBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (("http".equalsIgnoreCase(scheme) && serverPort != 80)
                || ("https".equalsIgnoreCase(scheme) && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        return url.toString();
    }
}
