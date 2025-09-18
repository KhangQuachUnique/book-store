package dao;

import model.Notification;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    // Đếm số thông báo CHƯA ĐỌC của một user
    public int countUnreadByUserId(long userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm thông báo chưa đọc cho user " + userId + ": " + e.getMessage());
        }
        return 0;
    }

    // Lấy tất cả thông báo của một user, sắp xếp mới nhất lên đầu
    public List<Notification> findByUserId(long userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setId(rs.getLong("id"));
                    notification.setUserId(rs.getLong("user_id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setType(rs.getString("type"));
                    notification.setCreatedAt(rs.getTimestamp("created_at"));
                    notification.setRead(rs.getBoolean("is_read"));
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách thông báo cho user " + userId + ": " + e.getMessage());
        }
        return notifications;
    }

    // Đánh dấu tất cả thông báo của user là đã đọc
    public void markAllAsRead(long userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi đánh dấu thông báo đã đọc cho user " + userId + ": " + e.getMessage());
        }
    }
}
