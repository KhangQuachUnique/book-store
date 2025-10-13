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

            // Chuyển cart thành List<Map> để tính toán
            List<Map<String, Object>> itemMaps = new ArrayList<>();
            for (CartItem item : items) {
                Map<String, Object> map = new HashMap<>();
                map.put("lineTotal", item.getSubtotal());
                itemMaps.add(map);
            }

            // Áp dụng mã khuyến mãi
            Map<String, Object> promoResult = promotionService.applyPromotion(promoCode, itemMaps);

            // Gửi phản hồi JSON
            writeJson(response, promoResult);

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
