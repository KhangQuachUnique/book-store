package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet xử lý IPN (Instant Payment Notification) từ MoMo
 * URL: /payment/momo/ipn
 * 
 * IPN LÀ GÌ?
 * - IPN là webhook mà MoMo server gọi đến để thông báo kết quả thanh toán
 * - Khác với callback (returnUrl) là user được redirect về, IPN là server-to-server
 * - IPN đảm bảo hệ thống luôn nhận được thông báo kể cả khi user đóng trình duyệt
 * 
 * WORKFLOW:
 * 1. User thanh toán trên MoMo
 * 2. MoMo server gửi POST request đến URL này với kết quả
 * 3. Server xử lý và cập nhật trạng thái đơn hàng
 * 4. Phải trả về status 204 để MoMo biết đã nhận được thông báo
 * 
 * LƯU Ý:
 * - Phải verify signature để đảm bảo request thật sự từ MoMo
 * - Phải xử lý idempotent (nhận nhiều lần cùng 1 IPN không gây lỗi)
 * - Luôn trả về 204 để MoMo không gửi lại
 */
@WebServlet("/payment/momo/ipn")
public class MoMoIpnServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MoMoIpnServlet.class.getName());
    private final Gson gson = new Gson();

    /**
     * Xử lý POST request từ MoMo server
     * MoMo gửi dữ liệu dạng JSON trong request body
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc toàn bộ JSON payload từ request body
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        }
        
        String payloadStr = payload.toString();
        log.info("Received MoMo IPN payload: " + payloadStr);
        
        try {
            // Parse chuỗi JSON thành JsonObject
            JsonObject json = gson.fromJson(payloadStr, JsonObject.class);
            
            // Kiểm tra có resultCode không (mã kết quả thanh toán)
            if (json != null && json.has("resultCode")) {
                // Lấy các thông tin cần thiết
                int resultCode = json.get("resultCode").getAsInt();  // 0 = thành công
                String orderId = json.has("orderId") ? json.get("orderId").getAsString() : "unknown";
                String message = json.has("message") ? json.get("message").getAsString() : "";
                
                // Kiểm tra kết quả thanh toán
                if (resultCode == 0) {
                    // THANH TOÁN THÀNH CÔNG
                    log.info("MoMo IPN: Payment successful for order " + orderId);
                    // TODO: Cập nhật trạng thái đơn hàng trong database
                    // orderService.updateOrderStatus(orderId, "PAID");
                    // TODO: Gửi email xác nhận đơn hàng
                    // TODO: Xóa giỏ hàng của user
                } else {
                    // THANH TOÁN THẤT BẠI
                    log.warning("MoMo IPN: Payment failed for order " + orderId + " - " + message);
                    // TODO: Cập nhật trạng thái đơn hàng thành FAILED
                    // orderService.updateOrderStatus(orderId, "FAILED");
                }
            }
            
            // QUAN TRỌNG: Luôn trả về 204 No Content để MoMo biết đã nhận được
            // Nếu không trả về 204, MoMo sẽ gửi lại IPN nhiều lần
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            
        } catch (Exception e) {
            // Xử lý lỗi nhưng vẫn phải trả về 204
            log.log(Level.SEVERE, "Error processing MoMo IPN", e);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // Vẫn phải acknowledge
        }
    }

    /**
     * Xử lý GET request (thường dùng để test endpoint)
     * MoMo thường gửi POST, GET chỉ là fallback
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.log(Level.INFO, "MoMo IPN endpoint accessed via GET");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}