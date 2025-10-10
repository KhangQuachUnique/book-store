package controller;

import service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/apply-promotion")
public class ApplyPromotionPageServlet extends HttpServlet {

    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Lấy danh sách promotion còn hiệu lực để hiển thị trong datalist
        List<model.Promotion> validPromotions = promotionService.getAllValidPromotions();
        request.setAttribute("validPromotions", validPromotions);

        // Hiển thị trang nhập mã
        request.getRequestDispatcher("/WEB-INF/views/apply-promotion.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        // ✅ Giả lập giỏ hàng
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(Map.of("title", "Sách A", "lineTotal", 80000.0));
        items.add(Map.of("title", "Sách B", "lineTotal", 240000.0));

        // ✅ Gọi service xử lý logic
        Map<String, Object> result = promotionService.applyPromotion(code, items);

        // ✅ Gửi kết quả sang JSP
        request.setAttribute("promotionResult", result);
        request.getRequestDispatcher("/WEB-INF/views/apply-promotion.jsp")
                .forward(request, response);

        // ✅ Gửi lại danh sách khuyến mãi (để khi refresh trang không mất gợi ý)
        List<model.Promotion> validPromotions = promotionService.getAllValidPromotions();
        request.setAttribute("validPromotions", validPromotions);

        request.getRequestDispatcher("/WEB-INF/views/apply-promotion.jsp")
                .forward(request, response);
    }
}
