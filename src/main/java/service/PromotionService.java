package service;

import dao.PromotionDAO;
import model.Promotion;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        return result;
    }

    // Client use
    public List<Promotion> getAllValidPromotions() {
        return promotionDAO.getAllValidPromotions();
    }

    // ===== ADMIN API =====
    public List<Promotion> listAll() {
        return promotionDAO.findAll();
    }

    public Promotion getById(Long id) {
        if (id == null) return null;
        return promotionDAO.findById(id);
    }

    public String create(String code, String discountStr, String expireAtLocal) {
        // Validate
        if (code == null || code.trim().isEmpty()) return "Mã khuyến mãi không được để trống";
        if (promotionDAO.existsByCode(code, null)) return "Mã khuyến mãi đã tồn tại";

        double discount;
        try {
            discount = Double.parseDouble(discountStr);
        } catch (Exception e) {
            return "Giá trị giảm giá không hợp lệ";
        }
        if (discount <= 0 || discount > 100) return "Giảm giá phải trong khoảng (0, 100]";

        OffsetDateTime expireAt = parseLocalDateTime(expireAtLocal);
        if (expireAt == null) return "Ngày hết hạn không hợp lệ";
        if (expireAt.isBefore(OffsetDateTime.now())) return "Ngày hết hạn phải ở tương lai";

        promotionDAO.create(code, discount, expireAt);
        return null;
    }

    public String update(Long id, String code, String discountStr, String expireAtLocal) {
        if (id == null) return "Thiếu ID";
        if (code == null || code.trim().isEmpty()) return "Mã khuyến mãi không được để trống";
        if (promotionDAO.existsByCode(code, id)) return "Mã khuyến mãi đã tồn tại";

        double discount;
        try {
            discount = Double.parseDouble(discountStr);
        } catch (Exception e) {
            return "Giá trị giảm giá không hợp lệ";
        }
        if (discount <= 0 || discount > 100) return "Giảm giá phải trong khoảng (0, 100]";

        OffsetDateTime expireAt = parseLocalDateTime(expireAtLocal);
        if (expireAt == null) return "Ngày hết hạn không hợp lệ";

        promotionDAO.update(id, code, discount, expireAt);
        return null;
    }

    public String delete(Long id) {
        if (id == null) return "Thiếu ID";
        promotionDAO.delete(id);
        return null;
    }

    // Convert from input type="datetime-local" (e.g., 2025-10-13T23:59) to OffsetDateTime in system zone
    private OffsetDateTime parseLocalDateTime(String local) {
        try {
            if (local == null || local.isEmpty()) return null;
            // datetime-local does not contain zone, assume system default and set offset accordingly
            var ldt = java.time.LocalDateTime.parse(local, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        } catch (Exception e) {
            return null;
        }
    }
}