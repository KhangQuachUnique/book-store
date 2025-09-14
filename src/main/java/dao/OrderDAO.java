package dao;

import model.Order;
import model.OrderItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Lấy danh sách đơn hàng theo userId
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toString());
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMethod(rs.getString("payment_method"));

                    // Lấy items của order
                    order.setItems(getOrderItemsByOrderId(conn, order.getId()));

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Lấy danh sách items cho một order
    private List<OrderItem> getOrderItemsByOrderId(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, b.title as book_title " +
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
                    items.add(item);
                }
            }
        }

        return items;
    }
}
