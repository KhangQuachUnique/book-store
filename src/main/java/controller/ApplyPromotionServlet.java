package controller;

import com.google.gson.Gson;
import service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/apply-promotion")
public class ApplyPromotionServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String code = request.getParameter("code");

        // ✅ Giả lập giỏ hàng (bạn có thể thay bằng dữ liệu thật từ DB hoặc session)
        List<Map<String, Object>> items = new ArrayList<>();

        Map<String, Object> book1 = Map.of(
                "bookId", 1,
                "title", "Sách A",
                "lineTotal", 80000.0
        );

        Map<String, Object> book2 = Map.of(
                "bookId", 2,
                "title", "Sách B",
                "lineTotal", 240000.0
        );

        items.add(book1);
        items.add(book2);

        Map<String, Object> result = promotionService.applyPromotion(code, items);

        response.getWriter().write(gson.toJson(result));
    }
}
