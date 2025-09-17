package dao;

import model.Order;
import model.OrderItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /**
     * Lấy danh sách tất cả các đơn hàng của một người dùng dựa trên userId,
     * sắp xếp theo thời gian tạo mới nhất.
     * Liên kết với bảng status để lấy tên trạng thái đơn hàng.
     *
     * @param userId ID của người dùng
     * @return Danh sách các đối tượng Order
     */
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.created_at, o.total_amount, o.payment_method, " +
                "o.status_id, s.name AS status_name " +
                "FROM orders o " +
                "JOIN status s ON o.status_id = s.id " +
                "WHERE o.user_id = ? ORDER BY o.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

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
     * Lấy thông tin chi tiết của một đơn hàng cụ thể dựa trên orderId.
     * Liên kết với bảng status để lấy tên trạng thái đơn hàng.
     *
     * @param orderId ID của đơn hàng
     * @return Đối tượng Order hoặc null nếu không tìm thấy
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

                    order.setItems(getOrderItemsByOrderId(conn, order.getId()));

                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Phương thức hỗ trợ để lấy danh sách các mặt hàng trong một đơn hàng.
     *
     * @param conn    Đối tượng Connection
     * @param orderId ID của đơn hàng
     * @return Danh sách các đối tượng OrderItem
     * @throws SQLException nếu có lỗi truy vấn SQL
     */
    private List<OrderItem> getOrderItemsByOrderId(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.quantity, oi.price, b.title AS book_title, b.thumbnail_url " +
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
                    items.add(item);
                }
            }
        }
        return items;
    }
}
