package controller;

import model.Address;
import model.CartItem;
import model.MoMoPaymentResponse;
import model.User;
import service.CartService;
import service.MoMoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Process payment - Handle payment method selection and initiate payment
 */
@WebServlet("/user/payment/process")
public class PaymentProcessServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(PaymentProcessServlet.class.getName());
    
    private final CartService cartService = new CartService();
    private final MoMoService momoService = new MoMoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Get payment method
            String paymentMethod = req.getParameter("paymentMethod");
            String addressId = req.getParameter("addressId");
            String notes = req.getParameter("notes");

            // Validate
            if (paymentMethod == null || paymentMethod.isEmpty()) {
                req.setAttribute("error", "Vui lòng chọn phương thức thanh toán");
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

            // Calculate total
            double subtotal = cartService.calculateSubtotal(cart);
            double total = subtotal + cartService.getShippingFee();

            // Store order info in session
            session.setAttribute("pendingOrderTotal", total);
            session.setAttribute("pendingOrderAddressId", addressId);
            session.setAttribute("pendingOrderNotes", notes);
            session.setAttribute("pendingOrderPaymentMethod", paymentMethod);

            // Handle payment based on method
            if ("momo".equalsIgnoreCase(paymentMethod)) {
                handleMoMoPayment(req, resp, user, total);
            } else {
                // COD or Bank Transfer - create order directly
                handleCODorBankPayment(req, resp, user, total, addressId, notes, paymentMethod);
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
                                   User user, double total) throws IOException, ServletException {
        try {
            // Generate order ID
            String orderId = "ORDER_" + System.currentTimeMillis() + "_" + user.getId();
            
            // Store order ID in session
            req.getSession().setAttribute("pendingMoMoOrderId", orderId);
            
            // Convert to long (VND - no decimal)
            long amount = Math.round(total);
            String orderInfo = "Thanh toán đơn hàng BookieCake - " + orderId;

            String baseUrl = buildBaseUrl(req);
            String redirectUrl = baseUrl + "/user/payment";
            String ipnUrl = baseUrl + "/user/payment/ipn";
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
     * Handle COD or Bank Transfer payment
     */
    private void handleCODorBankPayment(HttpServletRequest req, HttpServletResponse resp,
                                       User user, double total, String addressId, 
                                       String notes, String paymentMethod) 
            throws ServletException, IOException {
        // TODO: Create order in database
        // For now, just show success message
        req.setAttribute("success", "Đặt hàng thành công! Phương thức: " + paymentMethod);
        req.setAttribute("orderTotal", total);
        req.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(req, resp);
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
