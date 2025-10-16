package service;

import dao.OrderDAO;
import model.Order;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    /**
     * Lấy danh sách đơn theo user + trạng thái
     */
    public List<Order> getOrdersByUserAndStatus(Long userId, String statusParam) {
        return orderDAO.getOrdersByUserIdAndStatus(userId, statusParam);
    }

    /**
     * Tạo đơn hàng mới (tính giá và lưu)
     */
    public void createOrder(Order order) {
        orderDAO.createOrder(order);
    }
}