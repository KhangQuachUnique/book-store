package service;

import dao.OrderManagementDAO;
import model.Order;
import model.OrderStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagementService {
    private final OrderManagementDAO orderDAO = new OrderManagementDAO();

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public List<Order> searchOrders(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllOrders();
        }
        return orderDAO.searchOrders(keyword.trim());
    }

    public List<Order> getOrdersByStatus(String status) throws SQLException {
        return orderDAO.getOrdersByStatus(status);
    }

    public boolean updateOrderStatus(long orderId, String status) throws SQLException {
        if (!isValidStatus(status)) {
            return false;
        }
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public Order getOrderById(long orderId) throws SQLException {
        return orderDAO.getOrderById(orderId);
    }

    public boolean deleteOrder(long orderId) throws SQLException {
        return orderDAO.deleteOrder(orderId);
    }

    public Map<String, Object> getOrderStatistics(String period) throws SQLException {
        if (!"daily".equals(period) && !"monthly".equals(period)) {
            throw new IllegalArgumentException("Period must be either 'daily' or 'monthly'");
        }

        Map<String, Object> statistics = new HashMap<>();
        
        if ("daily".equals(period)) {
            statistics.put("revenue", orderDAO.getDailyRevenue());
            statistics.put("orderCount", orderDAO.getDailyOrderCount());
        } else {
            statistics.put("revenue", orderDAO.getMonthlyRevenue());
            statistics.put("orderCount", orderDAO.getMonthlyOrderCount());
        }

        return statistics;
    }

    public Map<String, Object> getTopSellingBooks(int limit) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        result.put("books", orderDAO.getTopSellingBooks(limit));
        return result;
    }

    public Order getOrderDetails(long orderId) throws SQLException {
        return orderDAO.getOrderById(orderId);
    }

    private boolean isValidStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        try {
            // Kiểm tra xem status có tồn tại trong OrderStatus enum không
            OrderStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
