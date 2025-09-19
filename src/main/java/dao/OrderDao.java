package dao;

import model.Order;
import model.OrderItem;
import model.Book;
import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {
    private final Connection conn;

    public OrderDao() throws SQLException {
        conn = DBConnection.getConnection();
    }

    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.name as user_name, u.email as user_email, u.phone as user_phone " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.id " +
                    "ORDER BY o.created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            Map<Long, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orderMap.put(order.getId(), order);
            }

            // Fetch order items for all orders
            if (!orderMap.isEmpty()) {
                fetchOrderItems(orderMap);
            }

            orders.addAll(orderMap.values());
        }

        return orders;
    }

    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.name as user_name, u.email as user_email, u.phone as user_phone " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.id " +
                    "WHERE o.status = ? " +
                    "ORDER BY o.created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            Map<Long, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orderMap.put(order.getId(), order);
            }

            // Fetch order items for filtered orders
            if (!orderMap.isEmpty()) {
                fetchOrderItems(orderMap);
            }

            orders.addAll(orderMap.values());
        }

        return orders;
    }

    public Order findById(Long id) throws SQLException {
        String sql = "SELECT o.*, u.name as user_name, u.email as user_email, u.phone as user_phone " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.id " +
                    "WHERE o.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                List<OrderItem> items = findOrderItems(order.getId());
                order.setItems(items);
                return order;
            }
        }

        return null;
    }

    public boolean updateStatus(Long id, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private List<OrderItem> findOrderItems(Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, b.title, b.author, b.thumbnail_url " +
                    "FROM order_items oi " +
                    "JOIN books b ON oi.book_id = b.id " +
                    "WHERE oi.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToOrderItem(rs));
            }
        }

        return items;
    }

    private void fetchOrderItems(Map<Long, Order> orderMap) throws SQLException {
        String sql = "SELECT oi.*, b.title, b.author, b.thumbnail_url " +
                    "FROM order_items oi " +
                    "JOIN books b ON oi.book_id = b.id " +
                    "WHERE oi.order_id IN (" + 
                    String.join(",", orderMap.keySet().stream().map(String::valueOf).toArray(String[]::new)) + 
                    ")";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = mapResultSetToOrderItem(rs);
                Order order = orderMap.get(item.getOrderId());
                if (order.getItems() == null) {
                    order.setItems(new ArrayList<>());
                }
                order.getItems().add(item);
            }
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPromotionId(rs.getLong("promotion_id"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Set user details
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("user_name"));
        user.setEmail(rs.getString("user_email"));
        user.setPhone(rs.getString("user_phone"));
        order.setUser(user);

        return order;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setBookId(rs.getLong("book_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));

        // Set book details
        Book book = new Book();
        book.setId(rs.getLong("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setThumbnailUrl(rs.getString("thumbnail_url"));
        item.setBook(book);

        return item;
    }
}