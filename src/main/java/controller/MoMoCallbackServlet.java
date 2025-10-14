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
 * Servlet xử lý callback từ MoMo sau khi user hoàn thành thanh toán
 * URL: /payment/momo/callback
 * 
 * CALLBACK LÀ GÌ?
 * - Callback là URL mà MoMo redirect user về sau khi thanh toán
 * - User sẽ thấy trang này sau khi thanh toán thành công/thất bại
 * - Khác với IPN (server-to-server), callback là browser redirect
 * 
 * WORKFLOW:
 * 1. User thanh toán trên MoMo app/website
 * 2. MoMo redirect user về URL này kèm parameters (resultCode, orderId, etc.)
 * 3. Server kiểm tra resultCode:
 *    - resultCode = 0: Thành công → Tạo Order, xóa Cart, redirect đến trang success
 *    - resultCode ≠ 0: Thất bại → Giữ Cart, redirect về payment page với error
 * 
 * DỮ LIỆU CẦN THIẾT:
 * - Trước khi redirect đến MoMo, đã lưu vào session:
 *   + pendingMoMoCart: Giỏ hàng
 *   + pendingMoMoAddress: Địa chỉ giao hàng
 *   + pendingMoMoPromotion: Mã khuyến mãi (nếu có)
 *   + pendingOrderTotal: Tổng tiền
 */
@WebServlet("/payment/momo/callback")
public class MoMoCallbackServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MoMoCallbackServlet.class.getName());

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    /**
     * Xử lý GET request - MoMo redirect user về đây
     * URL format: /payment/momo/callback?resultCode=0&orderId=xxx&message=xxx
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra user đã login chưa
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy các parameters từ MoMo callback
            String resultCode = req.getParameter("resultCode");  // "0" = thành công
            String message = req.getParameter("message");        // Thông báo từ MoMo
            String orderId = req.getParameter("orderId");        // Mã đơn hàng

            log.info("MoMo callback received - resultCode: " + resultCode + ", orderId: " + orderId);

            // Kiểm tra kết quả thanh toán
            if ("0".equals(resultCode)) {
                // THANH TOÁN THÀNH CÔNG
                handleSuccessfulPayment(req, resp, user, session);
            } else {
                // THANH TOÁN THẤT BẠI
                log.warning("MoMo payment failed - resultCode: " + resultCode + ", message: " + message);
                
                // Xóa dữ liệu MoMo tạm trong session
                clearPendingMoMoData(session);
                
                // Redirect về trang payment với thông báo lỗi
                session.setAttribute("paymentError", message != null ? message : "Thanh toán MoMo thất bại. Vui lòng thử lại.");
                resp.sendRedirect(req.getContextPath() + "/user/payment");
            }

        } catch (Exception e) {
            // Xử lý lỗi không mong muốn
            log.severe("Error processing MoMo callback: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("paymentError", "Có lỗi xảy ra khi xử lý thanh toán. Vui lòng liên hệ hỗ trợ.");
            resp.sendRedirect(req.getContextPath() + "/user/payment");
        }
    }

    /**
     * Xử lý thanh toán MoMo thành công - Tạo đơn hàng
     * 
     * QUY TRÌNH:
     * 1. Lấy dữ liệu đơn hàng tạm từ session (đã lưu trước khi redirect đến MoMo)
     * 2. Validate dữ liệu
     * 3. Tạo Order object với status PROCESSING
     * 4. Tạo OrderItems từ Cart
     * 5. Lưu Order vào database
     * 6. Gửi email xác nhận
     * 7. Xóa giỏ hàng
     * 8. Clear session data
     * 9. Redirect đến trang order tracking
     */
    @SuppressWarnings("unchecked")
    private void handleSuccessfulPayment(HttpServletRequest req, HttpServletResponse resp,
                                        User user, HttpSession session)
            throws IOException {
        try {
            // BƯỚC 1: Lấy dữ liệu tạm từ session (đã lưu trước khi redirect đến MoMo)
            List<CartItem> cart = (List<CartItem>) session.getAttribute("pendingMoMoCart");
            Address address = (Address) session.getAttribute("pendingMoMoAddress");
            Promotion promotion = (Promotion) session.getAttribute("pendingMoMoPromotion");
            Double total = (Double) session.getAttribute("pendingOrderTotal");

            // BƯỚC 2: Validate dữ liệu có đầy đủ không
            if (cart == null || cart.isEmpty() || address == null || total == null) {
                log.severe("Missing order data in session for MoMo payment");
                session.setAttribute("paymentError", "Thông tin đơn hàng không hợp lệ. Vui lòng thử lại.");
                resp.sendRedirect(req.getContextPath() + "/user/payment");
                return;
            }

            // BƯỚC 3: Tạo đối tượng Order
            Order order = new Order();
            order.setUser(user);                              // User đặt hàng
            order.setAddress(address);                        // Địa chỉ giao hàng
            order.setPromotion(promotion);                    // Mã khuyến mãi (nếu có)
            order.setPaymentMethod("MOMO");                   // Phương thức thanh toán
            order.setStatus(OrderStatus.PROCESSING);          // Trạng thái: Đang xử lý
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(total);                      // Tổng tiền

            // BƯỚC 4: Tạo OrderItems từ giỏ hàng
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart) {
                OrderItem orderItem = new OrderItem();
                orderItem.setBook(cartItem.getBook());        // Sách
                orderItem.setQuantity(cartItem.getQuantity()); // Số lượng
                orderItem.setOrder(order);                     // Liên kết với Order
                orderItems.add(orderItem);
            }
            order.setItems(orderItems);

            // BƯỚC 5: Lưu Order vào database
            orderService.createOrder(order);

            // BƯỚC 6: Gửi email xác nhận đơn hàng
            try {
                util.SendMailUtil.sendOrderConfirmationEmail(
                    user.getEmail(),
                    user.getName(),
                    String.valueOf(order.getId()),
                    String.format("%.0f", order.getTotalAmount())
                );
            } catch (Exception mailEx) {
                // Log warning nhưng không fail toàn bộ process
                log.warning("Không thể gửi email xác nhận đơn hàng: " + mailEx.getMessage());
            }

            // BƯỚC 7: Xóa giỏ hàng sau khi đặt hàng thành công
            cartService.clearCart(user.getId());

            // BƯỚC 8: Xóa dữ liệu tạm trong session
            clearPendingMoMoData(session);
            clearPendingOrderData(session);

            log.info("Order created successfully after MoMo payment - Order ID: " + order.getId());

            // BƯỚC 9: Redirect đến trang order tracking với thông báo thành công
            session.setAttribute("successMessage", "Thanh toán MoMo thành công! Đơn hàng của bạn đang được xử lý.");
            session.setAttribute("orderId", order.getId());
            resp.sendRedirect(req.getContextPath() + "/user/order-tracking");

        } catch (Exception e) {
            // Xử lý lỗi khi tạo đơn hàng
            log.severe("Error creating order after MoMo payment: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("paymentError", "Thanh toán thành công nhưng không thể tạo đơn hàng. Vui lòng liên hệ hỗ trợ.");
            resp.sendRedirect(req.getContextPath() + "/user/payment");
        }
    }

    /**
     * Xóa dữ liệu MoMo tạm thời khỏi session
     */
    private void clearPendingMoMoData(HttpSession session) {
        session.removeAttribute("pendingMoMoOrderId");
        session.removeAttribute("pendingMoMoCart");
        session.removeAttribute("pendingMoMoAddress");
        session.removeAttribute("pendingMoMoPromotion");
        session.removeAttribute("pendingMoMoNotes");
    }

    /**
     * Xóa dữ liệu đơn hàng tạm thời khỏi session
     */
    private void clearPendingOrderData(HttpSession session) {
        session.removeAttribute("pendingOrderTotal");
        session.removeAttribute("pendingOrderAddressId");
        session.removeAttribute("pendingOrderNotes");
        session.removeAttribute("pendingOrderPaymentMethod");
        session.removeAttribute("pendingOrderPromoCode");
    }
}
