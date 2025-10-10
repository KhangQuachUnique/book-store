package service;

import dao.PromotionDAO;
import model.Promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionService {
    private final PromotionDAO promotionDAO = new PromotionDAO();

    public Map<String, Object> applyPromotion(String code, List<Map<String, Object>> items) {
        Map<String, Object> result = new HashMap<>();

        if (code == null || code.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "Vui lòng nhập mã khuyến mãi.");
            return result;
        }

        Promotion promotion = promotionDAO.getPromotionByCode(code);
        if (promotion == null) {
            result.put("success", false);
            result.put("message", "Mã khuyến mãi không hợp lệ hoặc đã hết hạn.");
            return result;
        }

        double subtotal = 0;
        for (Map<String, Object> item : items) {
            Object lineTotal = item.get("lineTotal");
            if (lineTotal instanceof Number) {
                subtotal += ((Number) lineTotal).doubleValue();
            }
        }

        double discountAmount = subtotal * promotion.getDiscount() / 100.0;
        double finalTotal = subtotal - discountAmount;

        result.put("success", true);
        result.put("message", "Mã khuyến mãi đã được áp dụng thành công!");
        result.put("promotionCode", promotion.getCode());
        result.put("discountPercent", promotion.getDiscount());
        result.put("subtotal", subtotal);
        result.put("discountAmount", discountAmount);
        result.put("finalTotal", finalTotal);

        System.out.println("[INFO] Promotion applied successfully: " + promotion.getCode());
        return result;
    }

    // ✅ Hàm mới: lấy danh sách các promotion còn hiệu lực
    public List<Promotion> getAllValidPromotions() {
        return promotionDAO.getAllValidPromotions();
    }
}
