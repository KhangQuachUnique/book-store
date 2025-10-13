package controller;

import model.*;
import service.CartService;
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

/**
 * Handle MoMo payment callback after user completes payment
 */
@WebServlet("/payment/momo/callback")
public class MoMoCallbackServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MoMoCallbackServlet.class.getName());

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            // Get MoMo callback parameters
            String resultCode = req.getParameter("resultCode");
            String message = req.getParameter("message");
            String orderId = req.getParameter("orderId");

            log.info("MoMo callback received - resultCode: " + resultCode + ", orderId: " + orderId);

            // Check if payment was successful
            if ("0".equals(resultCode)) {
                // Payment successful - create order
                handleSuccessfulPayment(req, resp, user, session);
            } else {
                // Payment failed
                log.warning("MoMo payment failed - resultCode: " + resultCode + ", message: " + message);
                
                // Clear pending MoMo data
                clearPendingMoMoData(session);
                
                // Redirect back to payment page with error
                session.setAttribute("paymentError", message != null ? message : "Thanh toán MoMo thất bại. Vui lòng thử lại.");
                resp.sendRedirect(req.getContextPath() + "/user/payment");
            }

        } catch (Exception e) {
            log.severe("Error processing MoMo callback: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("paymentError", "Có lỗi xảy ra khi xử lý thanh toán. Vui lòng liên hệ hỗ trợ.");
            resp.sendRedirect(req.getContextPath() + "/user/payment");
        }
    }

    /**
     * Handle successful MoMo payment and create order
     */
    @SuppressWarnings("unchecked")
    private void handleSuccessfulPayment(HttpServletRequest req, HttpServletResponse resp,
                                        User user, HttpSession session)
            throws IOException {
        try {
            // Retrieve pending order data from session
            List<CartItem> cart = (List<CartItem>) session.getAttribute("pendingMoMoCart");
            Address address = (Address) session.getAttribute("pendingMoMoAddress");
            Promotion promotion = (Promotion) session.getAttribute("pendingMoMoPromotion");
            Double total = (Double) session.getAttribute("pendingOrderTotal");

            // Validate session data
            if (cart == null || cart.isEmpty() || address == null || total == null) {
                log.severe("Missing order data in session for MoMo payment");
                session.setAttribute("paymentError", "Thông tin đơn hàng không hợp lệ. Vui lòng thử lại.");
                resp.sendRedirect(req.getContextPath() + "/user/payment");
                return;
            }

            // Create order object
            Order order = new Order();
            order.setUser(user);
            order.setAddress(address);
            order.setPromotion(promotion);
            order.setPaymentMethod("MOMO");
            order.setStatus(OrderStatus.PROCESSING);
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(total);

            // Create order items from cart
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart) {
                OrderItem orderItem = new OrderItem();
                orderItem.setBook(cartItem.getBook());
                orderItem.setQuantity(cartItem.getQuantity());
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
                    String.format("%.0f₫", order.getTotalAmount())
                );
            } catch (Exception mailEx) {
                log.warning("Không thể gửi email xác nhận đơn hàng: " + mailEx.getMessage());
            }

            // Clear cart after successful order
            cartService.clearCart(user.getId());

            // Clear session data
            clearPendingMoMoData(session);
            clearPendingOrderData(session);

            log.info("Order created successfully after MoMo payment - Order ID: " + order.getId());

            // Redirect to order success page
            session.setAttribute("successMessage", "Thanh toán MoMo thành công! Đơn hàng của bạn đang được xử lý.");
            session.setAttribute("orderId", order.getId());
            resp.sendRedirect(req.getContextPath() + "/user/order-tracking");

        } catch (Exception e) {
            log.severe("Error creating order after MoMo payment: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("paymentError", "Thanh toán thành công nhưng không thể tạo đơn hàng. Vui lòng liên hệ hỗ trợ.");
            resp.sendRedirect(req.getContextPath() + "/user/payment");
        }
    }

    /**
     * Clear pending MoMo data from session
     */
    private void clearPendingMoMoData(HttpSession session) {
        session.removeAttribute("pendingMoMoOrderId");
        session.removeAttribute("pendingMoMoCart");
        session.removeAttribute("pendingMoMoAddress");
        session.removeAttribute("pendingMoMoPromotion");
        session.removeAttribute("pendingMoMoNotes");
    }

    /**
     * Clear pending order data from session
     */
    private void clearPendingOrderData(HttpSession session) {
        session.removeAttribute("pendingOrderTotal");
        session.removeAttribute("pendingOrderAddressId");
        session.removeAttribute("pendingOrderNotes");
        session.removeAttribute("pendingOrderPaymentMethod");
        session.removeAttribute("pendingOrderPromoCode");
    }
}
