package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.User;
import util.DBConnection;

public class UserDao {
    private Connection getConnection() throws SQLException {
        // Tạo kết nối (tuỳ cách bạn setup datasource/pool)
        return DBConnection.getConnection();
    }

    // Lưu user mới
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, phone, role, is_blocked, is_verified, verify_token, verify_expire) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.getIsBlocked() != null && user.getIsBlocked());
            ps.setBoolean(7, user.getIsVerified() != null && user.getIsVerified());
            ps.setString(8, user.getVerifyToken());
            ps.setTimestamp(9, user.getVerifyExpire());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm user bằng email
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

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

    public List<User> getAllUsers() throws SQLException {
        return getUsersByQuery("SELECT * FROM users");
    }

    public List<User> getAdmins() throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'admin'");
    }

    public List<User> getCustomers() throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'customer'");
    }

    public List<User> getBlockedUsers() throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE is_blocked = true");
    }

    public List<User> searchUsers(String query) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE name ILIKE ? OR email ILIKE ? OR phone ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        }
        return users;
    }

    private List<User> getUsersByQuery(String query) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    public User getUserById(long id) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }


    public void blockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = true, blocked_until = CURRENT_TIMESTAMP + INTERVAL '1 month', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void unblockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = false, blocked_until = null, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, role = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
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
