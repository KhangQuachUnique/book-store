package service;

import java.util.List;

import dao.OrderDAO;
import model.Order;

public class OrderService {

    public OrderService() {
    }

    /**
     * Lấy danh sách đơn hàng theo user và trạng thái
     * @param userId   ID người dùng
     * @param statusId trạng thái (all hoặc id cụ thể)
     * @return danh sách đơn hàng
     */
    public List<Order> getOrdersByUserAndStatus(Long userId, String statusId) {
        // Gọi thẳng xuống DAO (DAO đã tự xử lý "all")
        return OrderDAO.getOrdersByUserIdAndStatus(userId, statusId);
    }
}
