package service;

import dao.OrderDAO;
import model.Order;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagementService {
    private final OrderDAO orderDAO = new OrderDAO();

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public boolean updateOrderStatus(long orderId, String status) throws SQLException {
        if (!isValidStatus(status)) {
            return false;
        }
        return orderDAO.updateOrderStatus(orderId, status);
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
        return status != null && (
            status.equals("pending") ||
            status.equals("paid") ||
            status.equals("shipped") ||
            status.equals("completed") ||
            status.equals("cancelled")
        );
    }
}
