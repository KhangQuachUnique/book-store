package service;

import dao.OrderDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import util.JPAUtil;

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
     * 🧾 Tạo đơn hàng mới (tính giá và lưu)
     */
    public void createOrder(Order order) {
        orderDAO.createOrder(order);
    }
}