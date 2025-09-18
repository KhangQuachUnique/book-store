package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;
import util.DBConnection;

/**
 * Data Access Object for Category entity operations. Provides methods for category CRUD operations,
 * hierarchy management, and validation.
 *
 * @author BookStore Team
 * @version 1.0
 */
public class CategoryDao {

    private static final Logger log = Logger.getLogger(CategoryDao.class.getName());

    // ===== CORE CRUD OPERATIONS =====

    /**
     * Creates a new category in the database.
     *
     * @param category The category object to save
     * @return true if category was saved successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean create(Category category) {
        try (Connection conn = DBConnection.getConnection()) {
            return create(conn, category);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error creating category: " + category.getName(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Creates a new category using existing connection.
     *
     * @param conn The database connection to use
     * @param category The category object to save
     * @return true if category was saved successfully, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean create(Connection conn, Category category) throws SQLException {
        String sql =
                "INSERT INTO categories (name, parent_id, is_leaf, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());

            if (category.getParentId() != null) {
                ps.setLong(2, category.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }

            ps.setBoolean(3, category.getIsLeaf() != null ? category.getIsLeaf() : false);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Finds a category by its ID.
     *
     * @param id The category ID to search for
     * @return Optional containing Category if found, empty otherwise
     */
    public Optional<Category> findById(Long id) {
        try (Connection conn = DBConnection.getConnection()) {
            return findById(conn, id);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding category by ID: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Finds a category by its ID using existing connection.
     *
     * @param conn The database connection to use
     * @param id The category ID to search for
     * @return Optional containing Category if found, empty otherwise
     * @throws SQLException if database error occurs
     */
    public Optional<Category> findById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all categories.
     *
     * @return List of all categories ordered by ID descending
     */
    public List<Category> findAll() {
        try (Connection conn = DBConnection.getConnection()) {
            return findAll(conn);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding all categories", e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Retrieves all categories using existing connection.
     *
     * @param conn The database connection to use
     * @return List of all categories ordered by ID descending
     * @throws SQLException if database error occurs
     */
    public List<Category> findAll(Connection conn) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        }
        return categories;
    }

    /**
     * Updates an existing category's information.
     *
     * @param category The category object with updated information
     * @return true if category was updated successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean update(Category category) {
        try (Connection conn = DBConnection.getConnection()) {
            return update(conn, category);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating category: " + category.getId(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Updates an existing category using existing connection.
     *
     * @param conn The database connection to use
     * @param category The category object with updated information
     * @return true if category was updated successfully, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean update(Connection conn, Category category) throws SQLException {
        String sql =
                "UPDATE categories SET name = ?, parent_id = ?, is_leaf = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());

            if (category.getParentId() != null) {
                ps.setLong(2, category.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }

            ps.setBoolean(3, category.getIsLeaf() != null ? category.getIsLeaf() : false);
            ps.setLong(4, category.getId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a category from the database.
     *
     * @param id The ID of the category to delete
     * @return true if category was deleted successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean delete(Long id) {
        try (Connection conn = DBConnection.getConnection()) {
            return delete(conn, id);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting category: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Deletes a category using existing connection.
     *
     * @param conn The database connection to use
     * @param id The ID of the category to delete
     * @return true if category was deleted successfully, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean delete(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ===== HIERARCHY & VALIDATION OPERATIONS =====

    /**
     * Gets all root categories (categories with no parent).
     *
     * @return List of root categories
     */
    public List<Category> findRootCategories() {
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL ORDER BY name";
        return findCategoriesByCriteria(sql);
    }

    /**
     * Gets all child categories of a parent category.
     *
     * @param parentId The ID of the parent category
     * @return List of child categories
     */
    public List<Category> findChildCategories(Long parentId) {
        String sql = "SELECT * FROM categories WHERE parent_id = ? ORDER BY name";
        return findCategoriesByCriteria(sql, parentId);
    }

    /**
     * Gets all leaf categories (categories that can contain books).
     *
     * @return List of leaf categories
     */
    public List<Category> findLeafCategories() {
        String sql = "SELECT * FROM categories WHERE is_leaf = true ORDER BY name";
        return findCategoriesByCriteria(sql);
    }

    /**
     * Checks if a category name already exists (case-insensitive).
     *
     * @param name The category name to check
     * @return true if name exists, false otherwise
     */
    public boolean isCategoryNameExists(String name) {
        try (Connection conn = DBConnection.getConnection()) {
            return isCategoryNameExists(conn, name);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error checking category name existence: " + name, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Checks if a category name already exists using existing connection.
     *
     * @param conn The database connection to use
     * @param name The category name to check
     * @return true if name exists, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean isCategoryNameExists(Connection conn, String name) throws SQLException {
        String sql = "SELECT 1 FROM categories WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks if a category name exists excluding a specific category ID.
     *
     * @param name The category name to check
     * @param excludeId The category ID to exclude from the check
     * @return true if name exists (excluding the specified ID), false otherwise
     */
    public boolean isCategoryNameExistsExcluding(String name, Long excludeId) {
        String sql = "SELECT 1 FROM categories WHERE LOWER(name) = LOWER(?) AND id != ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setLong(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,
                    "Error checking category name existence excluding ID: " + excludeId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Checks if a category has any child categories.
     *
     * @param categoryId The category ID to check
     * @return true if category has children, false otherwise
     */
    public boolean hasChildCategories(Long categoryId) {
        String sql = "SELECT 1 FROM categories WHERE parent_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error checking child categories for: " + categoryId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Counts total number of categories.
     *
     * @return Total number of categories
     */
    public long countCategories() {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error counting categories", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    // ===== HELPER METHODS =====

    /**
     * Helper method to find categories by SQL criteria.
     *
     * @param sql The SQL query
     * @param params Parameters for the query
     * @return List of matching categories
     */
    private List<Category> findCategoriesByCriteria(String sql, Object... params) {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Long) {
                    ps.setLong(i + 1, (Long) params[i]);
                } else if (params[i] instanceof String) {
                    ps.setString(i + 1, (String) params[i]);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error executing category query: " + sql, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return categories;
    }

    /**
     * Maps a ResultSet row to a Category object.
     *
     * @param rs The ResultSet containing category data
     * @return Category object populated with data from ResultSet
     * @throws SQLException if database error occurs
     */
    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(rs.getLong("id"), rs.getString("name"),
                rs.getObject("parent_id") != null ? rs.getLong("parent_id") : null,
                rs.getBoolean("is_leaf"), rs.getTimestamp("created_at"));
    }
}
