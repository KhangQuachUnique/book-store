package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

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
                    users.add(extractUserFromResultSet(rs));
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
                users.add(extractUserFromResultSet(rs));
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
                    user = extractUserFromResultSet(rs);
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

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setIsBlocked(rs.getBoolean("is_blocked"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setBlockedUntil(rs.getTimestamp("blocked_until"));
        return user;
    }
}