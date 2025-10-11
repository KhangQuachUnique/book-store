package dao;

import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderStatusDAO {
    private static final Logger log = Logger.getLogger(OrderStatusDAO.class.getName());

    /**
     * Lấy toàn bộ danh sách trạng thái đơn hàng
     */
    public static List<OrderStatus> getAllStatuses() {
        try {
            // Trả về tất cả các enum values của OrderStatus
            List<OrderStatus> list = new ArrayList<>();
            for (OrderStatus status : OrderStatus.values()) {
                list.add(status);
            }
            return list;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy danh sách trạng thái đơn hàng", e);
            return new ArrayList<>();
        }
    }

}
