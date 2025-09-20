package controller;

import com.google.gson.Gson;
import dao.PromotionDAO;
import model.Promotion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/apply-promotion")
public class ApplyPromotionServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        String code = request.getParameter("code");

        if (code == null || code.trim().isEmpty()) {
            responseData.put("success", false);
            responseData.put("message", "Vui lòng nhập mã khuyến mãi.");
            response.getWriter().write(gson.toJson(responseData));
            return;
        }

        try {
            // ===== GIẢ LẬP GIỎ HÀNG (test) =====
            List<Map<String, Object>> items = new ArrayList<>();

            Map<String, Object> book1 = new HashMap<>();
            book1.put("bookId", 101);
            book1.put("title", "Sách A");
            book1.put("originalPrice", 100000.0);
            book1.put("currentPrice", 80000.0);
            book1.put("quantity", 1);
            book1.put("lineTotal", 80000.0);
            items.add(book1);

            Map<String, Object> book2 = new HashMap<>();
            book2.put("bookId", 102);
            book2.put("title", "Sách B");
            book2.put("originalPrice", 150000.0);
            book2.put("currentPrice", 120000.0);
            book2.put("quantity", 2);
            book2.put("lineTotal", 240000.0);
            items.add(book2);

            double subtotal = 80000 + 240000; // 320000

            // ===== KIỂM TRA PROMOTION =====
            PromotionDAO promotionDAO = new PromotionDAO();
            Promotion promotion = promotionDAO.getPromotionByCode(code);

            if (promotion != null) {
                double discountAmount = subtotal * promotion.getDiscount() / 100;
                double finalTotal = subtotal - discountAmount;

                responseData.put("success", true);
                responseData.put("message", "Mã khuyến mãi đã được áp dụng thành công!");
                responseData.put("promotionCode", promotion.getCode());
                responseData.put("promotionId", promotion.getId());
                responseData.put("discountPercent", promotion.getDiscount());
                responseData.put("subtotal", subtotal);
                responseData.put("discountAmount", discountAmount);
                responseData.put("finalTotal", finalTotal);
                responseData.put("items", items);
            } else {
                responseData.put("success", false);
                responseData.put("message", "Mã khuyến mãi không hợp lệ hoặc đã hết hạn.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("success", false);
            responseData.put("message", "Có lỗi xảy ra, vui lòng thử lại sau.");
        }

        response.getWriter().write(gson.toJson(responseData));
    }
}