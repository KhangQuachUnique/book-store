package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import util.DBConnection;

/**
 * Data Access Object for Order entity. Handles database operations for orders
 * table.
 */
public class OrderDao {

    /**
     * Retrieves all orders from the database.
     *
     * @return List of all orders ordered by creation date (newest first)
     */
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The order ID
     * @return Order object if found, null otherwise
     */
    public static Order getOrderById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId  The order ID
     * @param statusId The new status ID
     * @return true if update was successful, false otherwise
     */
    public static boolean updateOrderStatus(Long orderId, Long statusId) {
        String sql = "UPDATE orders SET status_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, statusId);
            ps.setLong(2, orderId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves orders by status ID.
     *
     * @param statusId The status ID to filter by
     * @return List of orders with the specified status
     */
    public static List<Order> getOrdersByStatus(Long statusId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Retrieves orders by user ID and status ID.
     *
     * @param userId   The user ID
     * @param statusId The status ID
     * @return List of orders for the user with the specified status
     */
    public static List<Order> getOrdersByUserIdAndStatus(Long userId, Long statusId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? AND status_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Retrieves orders by user ID and status (overloaded for Integer userId and
     * String status).
     *
     * @param userId The user ID
     * @param status The status filter ("all" returns all orders for user)
     * @return List of orders for the user with the specified status
     */
    public static List<Order> getOrdersByUserIdAndStatus(Integer userId, String status) {
        if ("all".equals(status)) {
            return getOrdersByUserIdWithStatusName(userId.longValue());
        }

        try {
            Long statusId = Long.parseLong(status);
            return getOrdersByUserIdAndStatusWithStatusName(userId.longValue(), statusId);
        } catch (NumberFormatException e) {
            // If status is not a number, return empty list
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all orders for a specific user with status name.
     *
     * @param userId The user ID
     * @return List of orders for the user with status names
     */
    public static List<Order> getOrdersByUserIdWithStatusName(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as status_name FROM orders o " +
                "LEFT JOIN status s ON o.status_id = s.id " +
                "WHERE o.user_id = ? ORDER BY o.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrderWithStatusName(rs);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Retrieves orders by user ID and status ID with status name.
     *
     * @param userId   The user ID
     * @param statusId The status ID
     * @return List of orders for the user with the specified status and status
     *         names
     */
    public static List<Order> getOrdersByUserIdAndStatusWithStatusName(Long userId, Long statusId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, s.name as status_name FROM orders o " +
                "LEFT JOIN status s ON o.status_id = s.id " +
                "WHERE o.user_id = ? AND o.status_id = ? ORDER BY o.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrderWithStatusName(rs);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Retrieves all orders for a specific user.
     *
     * @param userId The user ID
     * @return List of orders for the user
     */
    public static List<Order> getOrdersByUserId(Long userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Creates a new order in the database.
     *
     * @param order The order to create
     * @return The generated order ID, or null if creation failed
     */
    public static Long createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, payment_method, status_id, promotion_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getPaymentMethod());
            ps.setLong(4, order.getStatusId());
            if (order.getPromotionId() != null) {
                ps.setLong(5, order.getPromotionId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Maps a ResultSet row to an Order object.
     *
     * @param rs The ResultSet to map
     * @return Order object with data from the ResultSet
     * @throws SQLException if database access error occurs
     */
    private static Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setStatusId(rs.getLong("status_id"));
        order.setPromotionId(rs.getLong("promotion_id"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }

    /**
     * Maps a ResultSet row to an Order object with status name.
     *
     * @param rs The ResultSet to map
     * @return Order object with data from the ResultSet including status name
     * @throws SQLException if database access error occurs
     */
    private static Order mapResultSetToOrderWithStatusName(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setStatusId(rs.getLong("status_id"));
        order.setPromotionId(rs.getLong("promotion_id"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        order.setStatusName(rs.getString("status_name"));
        return order;
    }
}
