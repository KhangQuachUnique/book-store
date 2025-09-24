package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Order;
import model.OrderItem;
import util.DBConnection;

public class OrderDAO {

    public List<Order> getAllOrders() throws SQLException {
        String sql = """
            SELECT o.*, u.name as user_name, s.name as status_name
            FROM orders o 
            JOIN users u ON o.user_id = u.id
            JOIN status s ON o.status_id = s.id 
            ORDER BY o.created_at DESC
        """;
        
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setUserId(rs.getLong("user_id"));
                order.setTotalAmount(rs.getInt("total_amount"));
                order.setStatusId(rs.getLong("status_id"));
                order.setStatusName(rs.getString("status_name"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setCreatedAt(rs.getTimestamp("created_at").toString());
                order.setUserName(rs.getString("user_name"));
                order.setItems(getOrderItemsByOrderId(conn, order.getId()));
                orders.add(order);
            }
        }
        return orders;
    }

    public boolean updateOrderStatus(long orderId, String status) throws SQLException {
        // First get status ID from status table
        String getStatusIdSql = "SELECT id FROM status WHERE name = ?";
        long statusId;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getStatusIdSql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return false; // Status not found
            }
            statusId = rs.getLong("id");
        }

        // Then update the order
        String updateSql = "UPDATE orders SET status_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setLong(1, statusId);
            stmt.setLong(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Map<String, Object>> getDailyRevenue() throws SQLException {
        String sql = """
            SELECT DATE(created_at) as date, 
                   SUM(total_amount) as revenue,
                   COUNT(*) as order_count
            FROM orders o
            JOIN status s ON o.status_id = s.id
            WHERE s.name != 'cancelled'
            AND o.created_at >= CURRENT_DATE - INTERVAL '30 days'
            GROUP BY DATE(created_at)
            ORDER BY date DESC
        """;
        
        return getRevenueStats(sql);
    }

    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        String sql = """
            SELECT DATE_TRUNC('month', created_at) as date,
                   SUM(total_amount) as revenue,
                   COUNT(*) as order_count
            FROM orders o
            JOIN status s ON o.status_id = s.id
            WHERE s.name != 'cancelled'
            AND o.created_at >= CURRENT_DATE - INTERVAL '12 months'
            GROUP BY DATE_TRUNC('month', created_at)
            ORDER BY date DESC
        """;
        
        return getRevenueStats(sql);
    }

    private List<Map<String, Object>> getRevenueStats(String sql) throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("date", rs.getDate("date").toString());
                stat.put("revenue", rs.getInt("revenue"));
                stat.put("orderCount", rs.getInt("order_count"));
                stats.add(stat);
            }
        }
        return stats;
    }

    public List<Map<String, Object>> getDailyOrderCount() throws SQLException {
        return getDailyRevenue(); // This already includes order count
    }

    public List<Map<String, Object>> getMonthlyOrderCount() throws SQLException {
        return getMonthlyRevenue(); // This already includes order count
    }

    public List<Map<String, Object>> getTopSellingBooks(int limit) throws SQLException {
        String sql = """
            SELECT b.id, b.title, b.author, b.thumbnail_url,
                   SUM(oi.quantity) as total_sold,
                   SUM(oi.quantity * oi.price) as total_revenue
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            JOIN books b ON oi.book_id = b.id
            JOIN status s ON o.status_id = s.id
            WHERE s.name != 'cancelled'
            GROUP BY b.id, b.title, b.author, b.thumbnail_url
            ORDER BY total_sold DESC
            LIMIT ?
        """;
        
        List<Map<String, Object>> topBooks = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("id", rs.getLong("id"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("thumbnailUrl", rs.getString("thumbnail_url"));
                book.put("totalSold", rs.getInt("total_sold"));
                book.put("totalRevenue", rs.getInt("total_revenue"));
                topBooks.add(book);
            }
        }
        return topBooks;
    }

    public List<Order> getOrdersByUserIdAndStatus(Long userId, String statusId) {
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

            stmt.setLong(1, userId);
            if (statusId != null && !"all".equals(statusId)) {
                stmt.setLong(2, Long.parseLong(statusId));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toString());
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setStatusId(rs.getLong("status_id"));
                    order.setStatusName(rs.getString("status_name"));
                    order.setItems(getOrderItemsByOrderId(conn, order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private List<OrderItem> getOrderItemsByOrderId(Connection conn, Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.quantity, oi.price, b.title AS book_title, b.thumbnail_url, b.original_price, b.discount_rate " +
                "FROM order_items oi " +
                "JOIN books b ON oi.book_id = b.id " +
                "WHERE oi.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setBookTitle(rs.getString("book_title"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getInt("price"));
                    item.setThumbnailUrl(rs.getString("thumbnail_url"));
                    item.setOriginalPrice(rs.getDouble("original_price"));
                    item.setDiscountRate(rs.getInt("discount_rate"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public Order getOrderById(Long orderId) {
        String sql = "SELECT o.id, o.created_at, o.total_amount, o.payment_method, " +
                "o.status_id, s.name AS status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.id " +
                "WHERE o.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toString());
                    order.setTotalAmount(rs.getInt("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setStatusId(rs.getLong("status_id"));
                    order.setStatusName(rs.getString("status_name"));
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
