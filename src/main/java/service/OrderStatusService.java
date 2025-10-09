package service;

import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý danh sách trạng thái đơn hàng.
 * Giúp controller (hoặc JSP) lấy toàn bộ status hợp lệ để hiển thị hoặc lọc.
 */
public class OrderStatusService {

    /**
     * Lấy toàn bộ trạng thái đơn hàng (kèm "ALL" cho mục lọc tổng quát)
     */
    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add("ALL"); // Mục "Tất cả"
        for (OrderStatus status : OrderStatus.values()) {
            statuses.add(status.name());
        }
        return statuses;
    }
}
