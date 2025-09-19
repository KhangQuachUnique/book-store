package service;

import dao.OrderDao;
import model.Order;
import model.ApiResponse;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final OrderDao orderDao;

    public OrderService() throws SQLException {
        this.orderDao = new OrderDao();
    }

    public ApiResponse<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderDao.findAll();
            return new ApiResponse<>(true, "Orders retrieved successfully", orders);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Error retrieving orders: " + e.getMessage(), null);
        }
    }

    public ApiResponse<List<Order>> getOrdersByStatus(String status) {
        try {
            if (!isValidStatus(status)) {
                return new ApiResponse<>(false, "Invalid status provided", null);
            }
            List<Order> orders = orderDao.findByStatus(status);
            return new ApiResponse<>(true, "Orders retrieved successfully", orders);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Error retrieving orders: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Order> getOrderById(Long id) {
        try {
            Order order = orderDao.findById(id);
            if (order == null) {
                return new ApiResponse<>(false, "Order not found", null);
            }
            return new ApiResponse<>(true, "Order retrieved successfully", order);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Error retrieving order: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Order> updateOrderStatus(Long id, String newStatus) {
        try {
            // Validate status
            if (!isValidStatus(newStatus)) {
                return new ApiResponse<>(false, "Invalid status provided", null);
            }

            // Get current order
            Order order = orderDao.findById(id);
            if (order == null) {
                return new ApiResponse<>(false, "Order not found", null);
            }

            // Validate status transition
            if (!order.isValidStatusTransition(newStatus)) {
                return new ApiResponse<>(false, 
                    "Invalid status transition from " + order.getStatus() + " to " + newStatus, null);
            }

            // Update status
            boolean updated = orderDao.updateStatus(id, newStatus);
            if (!updated) {
                return new ApiResponse<>(false, "Failed to update order status", null);
            }

            // Return updated order
            order = orderDao.findById(id);
            return new ApiResponse<>(true, "Order status updated successfully", order);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Error updating order status: " + e.getMessage(), null);
        }
    }

    private boolean isValidStatus(String status) {
        if (status == null) return false;
        
        return status.equals(Order.STATUS_PENDING) ||
               status.equals(Order.STATUS_PAID) ||
               status.equals(Order.STATUS_SHIPPED) ||
               status.equals(Order.STATUS_COMPLETED) ||
               status.equals(Order.STATUS_CANCELLED);
    }
}