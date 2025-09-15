package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static util.DBConnection.getConnection;

public class UserDao {
    private static final Logger log = Logger.getLogger(UserDao.class.getName());
    private static final int PAGE_SIZE = 20; // Giới hạn 20 người dùng mỗi trang

    // Tìm user bằng verify_token
    public User findByVerifyToken(String token) {
        String sql = "SELECT * FROM users WHERE verify_token = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void markVerified(long userId) {
        String sql = "UPDATE users SET is_verified = TRUE, verify_token = NULL, verify_expire = NULL WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding user by email: " + email, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return Optional.empty();
    }

    // Lưu user mới
    public boolean save(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, phone, role, is_blocked, blocked_until, " +
                "is_verified, verify_token, verify_expire, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());

            // Các trường boolean/null-safe
            ps.setBoolean(6, user.getIsBlocked() != null && user.getIsBlocked());
            ps.setTimestamp(7, user.getBlockedUntil());
            ps.setBoolean(8, user.getIsVerified() != null && user.getIsVerified());
            ps.setString(9, user.getVerifyToken());
            ps.setTimestamp(10, user.getVerifyExpire());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error saving user: " + user.getEmail(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }



    // Cập nhật token và thời hạn xác thực mới
    public void updateVerifyToken(User user) {
        String sql = "UPDATE users SET verify_token = ?, verify_expire = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getVerifyToken());
            ps.setTimestamp(2, user.getVerifyExpire());
            ps.setLong(3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Helper: map từ ResultSet -> User object
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setIsBlocked(rs.getBoolean("is_blocked"));
        user.setBlockedUntil(rs.getTimestamp("blocked_until"));
        user.setIsVerified(rs.getBoolean("is_verified"));
        user.setVerifyToken(rs.getString("verify_token"));
        rs.getTimestamp("created_at");
        rs.getTimestamp("updated_at");
        user.setVerifyExpire(rs.getTimestamp("verify_expire"));
        return user;
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
            try(ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    boolean isBlocked = rs.getBoolean("is_blocked");
                    Timestamp until = rs.getTimestamp("blocked_until");
                    if (isBlocked) return until;
                    if (until != null && until.after(new Timestamp(System.currentTimeMillis()))) {
                        return until;
                    }
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting block info for: " + email, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return null;
    }

    public List<User> getAllUsers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?", page);
    }

    public List<User> getAdmins(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'admin' ORDER BY id LIMIT ? OFFSET ?", page);
    }

    public List<User> getCustomers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'customer' ORDER BY id LIMIT ? OFFSET ?", page);
    }

    public List<User> getBlockedUsers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE is_blocked = true ORDER BY id LIMIT ? OFFSET ?", page);
    }

    public List<User> searchUsers(String query, int page) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name ILIKE ? OR email ILIKE ? OR phone ILIKE ? ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setInt(4, PAGE_SIZE);
            pstmt.setInt(5, (page - 1) * PAGE_SIZE);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        }
        return users;
    }

    private List<User> getUsersByQuery(String query, int page) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, PAGE_SIZE);
            pstmt.setInt(2, (page - 1) * PAGE_SIZE);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        }
        return users;
    }

    public long countUsers(String queryType, String query) throws SQLException {
        String sql;
        if ("search".equals(queryType)) {
            sql = "SELECT COUNT(*) FROM users WHERE name ILIKE ? OR email ILIKE ? OR phone ILIKE ?";
        } else if ("listAdmins".equals(queryType)) {
            sql = "SELECT COUNT(*) FROM users WHERE role = 'admin'";
        } else if ("listUsers".equals(queryType)) {
            sql = "SELECT COUNT(*) FROM users WHERE role = 'customer'";
        } else if ("listBlocked".equals(queryType)) {
            sql = "SELECT COUNT(*) FROM users WHERE is_blocked = true";
        } else {
            sql = "SELECT COUNT(*) FROM users";
        }
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if ("search".equals(queryType)) {
                String searchTerm = "%" + query + "%";
                pstmt.setString(1, searchTerm);
                pstmt.setString(2, searchTerm);
                pstmt.setString(3, searchTerm);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0;
    }

    public User getUserById(long id) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = mapRow(rs);
                }
            }
        }
        return user;
    }

    public void deleteUser(long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void blockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = true, blocked_until = CURRENT_TIMESTAMP + INTERVAL '1 month', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void unblockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = false, blocked_until = null, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, role = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getRole());
            pstmt.setLong(5, user.getId());
            pstmt.executeUpdate();
        }
    }

    public void createAdmin(User user) throws SQLException {
        createUserWithRole(user, "admin");
    }

    public void createUser(User user) throws SQLException {
        createUserWithRole(user, "customer");
    }

    private void createUserWithRole(User user, String role) throws SQLException {
        String query = "INSERT INTO users (name, email, password_hash, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, role);
            pstmt.executeUpdate();
        }
    }
}