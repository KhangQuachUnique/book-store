package dao;

import util.DBConnection;
import model.User;
import java.sql.*;
import java.util.Optional;

public class UserDao {
	
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Connection getConnection() throws SQLException {
    	return DBConnection.getConnection();
	}

    public boolean save(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, phone, role, is_blocked, blocked_until, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, now())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.getIsBlocked() != null ? user.getIsBlocked() : false);
            ps.setTimestamp(7, user.getBlockedUntil()); // có thể null
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Trả về true/false
    public boolean isUserBlocked(String email) {
        return getBlockInfo(email) != null;
    }

    // Trả về thời gian unblock (nếu có), null nếu không bị block
    public Timestamp getBlockInfo(String email) {
        String sql = "SELECT is_blocked, blocked_until FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean isBlocked = rs.getBoolean("is_blocked");
                Timestamp until = rs.getTimestamp("blocked_until");

                // nếu is_blocked = true hoặc until > now → đang bị block
                if (isBlocked) return until; 
                if (until != null && until.after(new Timestamp(System.currentTimeMillis()))) {
                    return until;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("phone"),
                rs.getString("role"),
                rs.getBoolean("is_blocked"),
                rs.getTimestamp("blocked_until")
        );
    }
}
