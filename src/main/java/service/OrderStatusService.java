package service;

import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service quản lý danh sách trạng thái đơn hàng.
 * Giúp controller (hoặc JSP) lấy toàn bộ status hợp lệ để hiển thị hoặc lọc.
 */
public class OrderStatusService {

    // ✅ Map ánh xạ status → tên hiển thị
    private static final Map<OrderStatus, String> DISPLAY_NAMES = Map.of(
            OrderStatus.PENDING_PAYMENT, "Đang chờ",
            OrderStatus.PROCESSING, "Đang xử lý",
            OrderStatus.WAITING_DELIVERY, "Đang giao",
            OrderStatus.DELIVERED, "Đã giao",
            OrderStatus.CANCELED, "Đã hủy"
    );

    // ✅ Map ánh xạ status → class CSS (để đổi màu nhanh)
    private static final Map<OrderStatus, String> CSS_CLASSES = Map.of(
            OrderStatus.PENDING_PAYMENT, "pending",
            OrderStatus.PROCESSING, "processing",
            OrderStatus.WAITING_DELIVERY, "shipping",
            OrderStatus.DELIVERED, "delivered",
            OrderStatus.CANCELED, "cancelled"
    );

    /**
     * Lấy toàn bộ trạng thái (kèm “ALL” cho mục “Tất cả”)
     */
    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add("ALL");
        for (OrderStatus s : OrderStatus.values()) {
            statuses.add(s.name());
        }
        return statuses;
    }

    /**
     * Lấy tên hiển thị tiếng Việt
     */
    public String getDisplayName(OrderStatus status) {
        return DISPLAY_NAMES.getOrDefault(status, "Không xác định");
    }

    /**
     * Lấy tên class CSS tương ứng
     */
    public String getCssClass(OrderStatus status) {
        return CSS_CLASSES.getOrDefault(status, "unknown");
    }
}