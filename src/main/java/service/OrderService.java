package service;

import dao.OrderDao;
import model.Order;
import java.util.List;

public class OrderService {

    public static List<Order> getAllOrders() {
        return OrderDao.getAllOrders();
    }

    public static Order getOrderById(int id) {
        return OrderDao.getOrderById(id);
    }

    public static boolean updateOrderStatus(int orderId, String newStatus) {
        // Validate status
        if (!isValidStatus(newStatus)) {
            return false;
        }

        // Check if status transition is valid
        Order currentOrder = OrderDao.getOrderById(orderId);
        if (currentOrder == null) {
            return false;
        }

        if (!isValidStatusTransition(currentOrder.getStatus(), newStatus)) {
            return false;
        }

        return OrderDao.updateOrderStatus(orderId, newStatus);
    }

    public static boolean updateOrderStatusWithNotes(int orderId, String newStatus, String notes) {
        // Validate status
        if (!isValidStatus(newStatus)) {
            return false;
        }

        // Check if status transition is valid
        Order currentOrder = OrderDao.getOrderById(orderId);
        if (currentOrder == null) {
            return false;
        }

        if (!isValidStatusTransition(currentOrder.getStatus(), newStatus)) {
            return false;
        }

        return OrderDao.updateOrderStatusWithNotes(orderId, newStatus, notes);
    }

    public static List<Order> getOrdersByStatus(String status) {
        return OrderDao.getOrdersByStatus(status);
    }

    private static boolean isValidStatus(String status) {
        return status != null &&
                (status.equals("PENDING") ||
                        status.equals("CONFIRMED") ||
                        status.equals("DELIVERY") ||
                        status.equals("CANCELLED"));
    }

    private static boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case "PENDING":
                return newStatus.equals("CONFIRMED") || newStatus.equals("CANCELLED");
            case "CONFIRMED":
                return newStatus.equals("DELIVERY") || newStatus.equals("CANCELLED");
            case "DELIVERY":
                return false; // Cannot change from DELIVERY
            case "CANCELLED":
                return false; // Cannot change from CANCELLED
            default:
                return false;
        }
    }
}
