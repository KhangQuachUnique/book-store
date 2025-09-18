package dao;

import model.Order;
import model.OrderItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> getOrdersByUserIdAndStatus(int userId, String statusId) {
        List<Order> orders = new ArrayList<>();
        String sql;

        if (statusId == null || "all".equals(statusId)) {
            sql = "SELECT o.id, o.created_at, o.total_amount, o.payment_method, " +
                    "o.status_id, s.name AS status_name " +
                    "FROM orders o " +
                    "JOIN status s ON o.status_id = s.id " +
                    "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        } else {
            sql = "SELECT o.id, o.created_at, o.total_amount, o.payment_method, " +
                    "o.status_id, s.name AS status_name " +
                    "FROM orders o " +
                    "JOIN status s ON o.status_id = s.id " +
                    "WHERE o.user_id = ? AND o.status_id = ? ORDER BY o.created_at DESC";
        }


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            if (statusId != null && !"all".equals(statusId)) {
                stmt.setInt(2, Integer.parseInt(statusId));
            }


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toString());
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));

                    order.setStatusId(rs.getInt("status_id"));
                    order.setStatusName(rs.getString("status_name"));

                    // Lấy danh sách sản phẩm của đơn
                    order.setItems(getOrderItemsByOrderId(conn, order.getId()));

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Hỗ trợ lấy danh sách OrderItem theo orderId.
     */
    // Trong OrderDAO.java
    private List<OrderItem> getOrderItemsByOrderId(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        // Sửa câu lệnh SQL để lấy các cột mới
        String sql = "SELECT oi.quantity, oi.price, b.title AS book_title, b.thumbnail_url, b.original_price, b.discount_rate " +
                "FROM order_items oi " +
                "JOIN books b ON oi.book_id = b.id " +
                "WHERE oi.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setBookTitle(rs.getString("book_title"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getInt("price"));
                    item.setThumbnailUrl(rs.getString("thumbnail_url"));

                    // Gán các giá trị mới từ ResultSet
                    item.setOriginalPrice(rs.getDouble("original_price"));
                    item.setDiscountRate(rs.getInt("discount_rate"));

                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * Lấy thông tin chi tiết của 1 đơn hàng theo orderId
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.id, o.created_at, o.total_amount, o.payment_method, " +
                "o.status_id, s.name AS status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.id " +
                "WHERE o.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toString());
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));

                    order.setStatusId(rs.getInt("status_id"));
                    order.setStatusName(rs.getString("status_name"));

                    // Lấy danh sách sản phẩm trong đơn
                    order.setItems(getOrderItemsByOrderId(conn, order.getId()));

                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
