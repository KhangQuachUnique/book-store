package controller;

import com.google.gson.Gson;
import model.Cart;
import model.CartItem;
import model.User;
import service.CartService;
import service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/user/payment/apply-promotion")
public class ApplyPromotionServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Đảm bảo phản hồi trả về JSON UTF-8
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false); // Tránh tạo session mới nếu chưa có
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Kiểm tra user đăng nhập
            if (session == null || session.getAttribute("user") == null) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Bạn cần đăng nhập trước.");
                writeJson(response, jsonResponse);
                return;
            }

            User user = (User) session.getAttribute("user");
            String promoCode = request.getParameter("promoCode");

            // Lấy giỏ hàng hiện tại
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> items = cart != null && cart.getItems() != null ? cart.getItems() : Collections.emptyList();

            // Tính tổng giỏ hàng (subtotal)
            double subtotal = 0;
            for (CartItem item : items) {
                subtotal += item.getSubtotal();
            }

            // Giả sử phí vận chuyển cố định
            double shippingCost = 30000; // Có thể lấy từ giỏ hàng nếu có
            double totalBeforeDiscount = subtotal + shippingCost;

            // Chuyển subtotal thành dạng List<Map<String, Object>> phù hợp với yêu cầu của applyPromotion
            Map<String, Object> map = new HashMap<>();
            map.put("lineTotal", totalBeforeDiscount);  // Cung cấp tổng (sản phẩm + phí ship) cho applyPromotion
            List<Map<String, Object>> itemMaps = new ArrayList<>();
            itemMaps.add(map);

            // Áp dụng mã khuyến mãi
            Map<String, Object> promoResult = promotionService.applyPromotion(promoCode, itemMaps);

            // Lấy discountAmount từ kết quả trả về của applyPromotion
            double discountAmount = (double) promoResult.get("discountAmount");
            double finalTotal = totalBeforeDiscount - discountAmount;

            // Trả về kết quả
            jsonResponse.put("success", true);
            jsonResponse.put("subtotal", subtotal);
            jsonResponse.put("shipping", shippingCost);
            jsonResponse.put("discountAmount", discountAmount);
            jsonResponse.put("finalTotal", finalTotal);  // Trả về tổng sau khi giảm giá
            jsonResponse.put("message", "Mã khuyến mãi đã được áp dụng thành công!");
            jsonResponse.put("promotionCode", promoCode);
            jsonResponse.put("discountPercent", promoResult.get("discountPercent"));  // Phần trăm giảm giá

            // Gửi phản hồi JSON
            writeJson(response, jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Đã xảy ra lỗi khi áp dụng mã khuyến mãi.");
            writeJson(response, jsonResponse);
        }
    }

    // Hàm helper xuất JSON ra response
    private void writeJson(HttpServletResponse response, Map<String, Object> data) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        response.getWriter().write(json);
    }
}
