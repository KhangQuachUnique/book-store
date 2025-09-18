package service;

import dao.OrderDao;
import model.Order;
import java.util.List;

/**
 * Service class for Order operations.
 * Handles business logic for order management.
 */
public class OrderService {

    public static List<Order> getAllOrders() {
        return OrderDao.getAllOrders();
    }

    public static Order getOrderById(Long id) {
        return OrderDao.getOrderById(id);
    }

    /**
     * Updates order status with validation.
     * @param orderId The order ID
     * @param newStatusId The new status ID
     * @return true if update was successful, false otherwise
     */
    public static boolean updateOrderStatus(Long orderId, Long newStatusId) {
        // Validate status
        if (!isValidStatusId(newStatusId)) {
            return false;
        }

        // Check if status transition is valid
        Order currentOrder = OrderDao.getOrderById(orderId);
        if (currentOrder == null) {
            return false;
        }

        if (!isValidStatusTransition(currentOrder.getStatusId(), newStatusId)) {
            return false;
        }

        return OrderDao.updateOrderStatus(orderId, newStatusId);
    }

    public static List<Order> getOrdersByStatus(Long statusId) {
        return OrderDao.getOrdersByStatus(statusId);
    }

    /**
     * Validates if the status ID is valid.
     * @param statusId The status ID to validate
     * @return true if valid, false otherwise
     */
    private static boolean isValidStatusId(Long statusId) {
        // Assuming status IDs are 1-4 for PENDING, CONFIRMED, DELIVERY, CANCELLED
        return statusId != null && statusId >= 1 && statusId <= 4;
    }

    /**
     * Validates if the status transition is allowed.
     * @param currentStatusId The current status ID
     * @param newStatusId The new status ID
     * @return true if transition is valid, false otherwise
     */
    private static boolean isValidStatusTransition(Long currentStatusId, Long newStatusId) {
        if (currentStatusId == null || newStatusId == null) {
            return false;
        }
        
        // Define valid status transitions based on status IDs
        // Assuming: 1=PENDING, 2=CONFIRMED, 3=DELIVERY, 4=CANCELLED
        switch (currentStatusId.intValue()) {
            case 1: // PENDING
                return newStatusId == 2 || newStatusId == 4; // Can go to CONFIRMED or CANCELLED
            case 2: // CONFIRMED
                return newStatusId == 3 || newStatusId == 4; // Can go to DELIVERY or CANCELLED
            case 3: // DELIVERY
                return false; // Cannot change from DELIVERY
            case 4: // CANCELLED
                return false; // Cannot change from CANCELLED
            default:
                return false;
        }
    }
}
