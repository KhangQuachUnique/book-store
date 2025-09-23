package dao;

import model.ViewHistoryItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewHistoryDao {

    public List<ViewHistoryItem> getHistoryByUserId(Long userId) {
        List<ViewHistoryItem> list = new ArrayList<>();
        String sql = """
            SELECT v.id, v.viewed_at, b.id AS book_id, b.title, b.thumbnail_url
            FROM viewed v
            JOIN books b ON v.book_id = b.id
            WHERE v.user_id = ?
            ORDER BY v.viewed_at DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ViewHistoryItem item = new ViewHistoryItem();
                item.setId(rs.getLong("id"));
                item.setBookId(rs.getLong("book_id"));
                item.setTitle(rs.getString("title"));
                item.setThumbnail(rs.getString("thumbnail_url"));
                item.setViewedAt(rs.getTimestamp("viewed_at"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addHistory(Long userId, Long bookId) {
        String sql = "INSERT INTO viewed (user_id, book_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
