package dao;

import model.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.DBConnection.getConnection;

/**
 * Data Access Object for User entity operations.
 * Provides methods for user CRUD operations, authentication, verification, and administration.
 * 
 * @author BookStore Team
 * @version 1.0
 */
public class UserDao {
    
    private static final Logger log = Logger.getLogger(UserDao.class.getName());
    private static final int PAGE_SIZE = 20;

    // ===== CORE CRUD OPERATIONS =====
    
    /**
     * Creates a new user in the database.
     * 
     * @param user The user object to save
     * @return true if user was saved successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
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

            // Boolean/null-safe fields
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

    /**
     * Finds a user by their ID.
     * 
     * @param id The user ID to search for
     * @return User object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    public User getUserById(long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding user by ID: " + id, e);
            throw e;
        }
        return null;
    }

    /**
     * Finds a user by their email address.
     * 
     * @param email The email address to search for
     * @return Optional containing User if found, empty otherwise
     * @throws RuntimeException if database error occurs
     */
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding user by email: " + email, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return Optional.empty();
    }

    /**
     * Gets a user by their email address (compatibility method).
     * 
     * @param email The email address to search for
     * @return User if found, null otherwise
     */
    public User getUserByEmail(String email) {
        Optional<User> user = findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Updates a user's password hash.
     * 
     * @param user The user object with updated password hash
     * @throws SQLException if database error occurs
     */
    public void updateUserPasswordHash(User user) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPasswordHash());
            ps.setLong(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating user password hash: " + user.getId(), e);
            throw e;
        }
    }

    /**
     * Updates an existing user's information.
     * 
     * @param user The user object with updated information
     * @throws SQLException if database error occurs
     */
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
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating user: " + user.getId(), e);
            throw e;
        }
    }

    /**
     * Deletes a user from the database.
     * 
     * @param id The ID of the user to delete
     * @throws SQLException if database error occurs
     */
    public void deleteUser(long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting user: " + id, e);
            throw e;
        }
    }
    
    // ===== AUTHENTICATION & VERIFICATION OPERATIONS =====
    
    /**
     * Finds a user by their verification token.
     * 
     * @param token The verification token to search for
     * @return User object if found, null otherwise
     */
    public User findByVerifyToken(String token) {
        String sql = "SELECT * FROM users WHERE verify_token = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding user by verify token", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return null;
    }

    /**
     * Marks a user as verified and clears verification token.
     * 
     * @param userId The ID of the user to verify
     */
    public void markVerified(long userId) {
        String sql = "UPDATE users SET is_verified = TRUE, verify_token = NULL, verify_expire = NULL WHERE id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error marking user as verified: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Updates user's verification token and expiry.
     * 
     * @param user The user object containing new verification data
     */
    public void updateVerifyToken(User user) {
        String sql = "UPDATE users SET verify_token = ?, verify_expire = ? WHERE id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getVerifyToken());
            ps.setTimestamp(2, user.getVerifyExpire());
            ps.setLong(3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating verify token for user: " + user.getId(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Updates user password and clears verification token (for password reset).
     * 
     * @param userId The ID of the user
     * @param hashedPassword The new hashed password
     */
    public void updatePasswordAndClearToken(Long userId, String hashedPassword) {
        String sql = "UPDATE users SET password_hash = ?, verify_token = NULL, verify_expire = NULL, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating password for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }
    
    // ===== USER BLOCKING OPERATIONS =====
    
    /**
     * Checks if a user is currently blocked.
     * 
     * @param email The email of the user to check
     * @return true if user is blocked, false otherwise
     */
    public boolean isUserBlocked(String email) {
        return getBlockInfo(email) != null;
    }

    /**
     * Gets block information for a user.
     * 
     * @param email The email of the user
     * @return Timestamp of when the block expires, null if not blocked
     */
    public Timestamp getBlockInfo(String email) {
        String sql = "SELECT is_blocked, blocked_until FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
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

    /**
     * Blocks a user for one month.
     * 
     * @param id The ID of the user to block
     * @throws SQLException if database error occurs
     */
    public void blockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = true, blocked_until = CURRENT_TIMESTAMP + INTERVAL '1 month', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error blocking user: " + id, e);
            throw e;
        }
    }

    /**
     * Unblocks a user.
     * 
     * @param id The ID of the user to unblock
     * @throws SQLException if database error occurs
     */
    public void unblockUser(long id) throws SQLException {
        String query = "UPDATE users SET is_blocked = false, blocked_until = null, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error unblocking user: " + id, e);
            throw e;
        }
    }

    // ===== LIST & SEARCH OPERATIONS =====
    
    /**
     * Gets all users with pagination.
     * 
     * @param page The page number (1-based)
     * @return List of users for the requested page
     * @throws SQLException if database error occurs
     */
    public List<User> getAllUsers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?", page);
    }

    /**
     * Gets admin users with pagination.
     * 
     * @param page The page number (1-based)
     * @return List of admin users for the requested page
     * @throws SQLException if database error occurs
     */
    public List<User> getAdmins(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'admin' ORDER BY id LIMIT ? OFFSET ?", page);
    }

    /**
     * Gets customer users with pagination.
     * 
     * @param page The page number (1-based)
     * @return List of customer users for the requested page
     * @throws SQLException if database error occurs
     */
    public List<User> getCustomers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE role = 'customer' ORDER BY id LIMIT ? OFFSET ?", page);
    }

    /**
     * Gets blocked users with pagination.
     * 
     * @param page The page number (1-based)
     * @return List of blocked users for the requested page
     * @throws SQLException if database error occurs
     */
    public List<User> getBlockedUsers(int page) throws SQLException {
        return getUsersByQuery("SELECT * FROM users WHERE is_blocked = true ORDER BY id LIMIT ? OFFSET ?", page);
    }

    /**
     * Searches users by name, email, or phone with pagination.
     * 
     * @param query The search query
     * @param page The page number (1-based)
     * @return List of matching users for the requested page
     * @throws SQLException if database error occurs
     */
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
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error searching users with query: " + query, e);
            throw e;
        }
        return users;
    }

    /**
     * Counts users based on query type and search criteria.
     * 
     * @param queryType The type of query (search, listAdmins, listUsers, listBlocked, or all)
     * @param query The search query (only used for search type)
     * @return The count of matching users
     * @throws SQLException if database error occurs
     */
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
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error counting users with queryType: " + queryType, e);
            throw e;
        }
        return 0;
    }

    // ===== ADMIN OPERATIONS =====
    
    /**
     * Creates a new admin user.
     * 
     * @param user The user object to create as admin
     * @throws SQLException if database error occurs
     */
    public void createAdmin(User user) throws SQLException {
        createUserWithRole(user, "admin");
    }

    /**
     * Creates a new customer user.
     * 
     * @param user The user object to create as customer
     * @throws SQLException if database error occurs
     */
    public void createUser(User user) throws SQLException {
        createUserWithRole(user, "customer");
    }

    // ===== HELPER METHODS =====
    
    /**
     * Helper method to execute paginated user queries.
     * 
     * @param query The SQL query with LIMIT and OFFSET placeholders
     * @param page The page number (1-based)
     * @return List of users for the requested page
     * @throws SQLException if database error occurs
     */
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
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error executing user query: " + query, e);
            throw e;
        }
        return users;
    }

    /**
     * Helper method to create user with specific role.
     * 
     * @param user The user object to create
     * @param role The role to assign to the user
     * @throws SQLException if database error occurs
     */
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
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error creating user with role: " + role, e);
            throw e;
        }
    }

    /**
     * Maps a ResultSet row to a User object.
     * 
     * @param rs The ResultSet containing user data
     * @return User object populated with data from ResultSet
     * @throws SQLException if database error occurs
     */
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
        user.setVerifyExpire(rs.getTimestamp("verify_expire"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}