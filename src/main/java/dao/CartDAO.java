package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.CartItem;
import util.DBConnection;

/**
 * Data Access Object for Cart entity operations. Provides methods for shopping
 * cart management
 * including adding, removing, and querying cart items.
 *
 * @author BookStore Team
 * @version 1.0
 */
public class CartDAO {

    private static final Logger log = Logger.getLogger(CartDAO.class.getName());

    // ===== CORE CART OPERATIONS =====

    /**
     * Adds an item to the user's cart. If the item already exists, increases the
     * quantity.
     *
     * @param userId   The ID of the user
     * @param bookId   The ID of the book to add
     * @param quantity The quantity to add
     * @return true if item was added successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean addToCart(long userId, long bookId, int quantity) {
        String sql = "INSERT INTO carts(user_id, book_id, quantity, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP) "
                + "ON CONFLICT (user_id, book_id) DO UPDATE SET quantity = carts.quantity + EXCLUDED.quantity, "
                + "updated_at = CURRENT_TIMESTAMP";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error adding to cart - User: " + userId + ", Book: " + bookId,
                    e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * @param cartId   The ID of the cart item
     * @param quantity The new quantity
     * @return true if quantity was updated successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean updateCartItemQuantity(long cartId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(cartId);
        }

        String sql = "UPDATE carts SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, cartId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating cart item quantity: " + cartId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Updates the quantity of a cart item by user and book.
     *
     * @param userId   The ID of the user
     * @param bookId   The ID of the book
     * @param quantity The new quantity
     * @return true if quantity was updated successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean updateCartItemQuantity(long userId, long bookId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, bookId);
        }

        String sql = "UPDATE carts SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ? AND book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, userId);
            ps.setLong(3, bookId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE,
                    "Error updating cart item - User: " + userId + ", Book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Removes a specific item from the cart by cart ID.
     *
     * @param cartId The ID of the cart item to remove
     * @return true if item was removed successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean removeFromCart(long cartId) {
        String sql = "DELETE FROM carts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error removing cart item: " + cartId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Removes a specific item from the cart by user and book.
     *
     * @param userId The ID of the user
     * @param bookId The ID of the book to remove
     * @return true if item was removed successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean removeFromCart(long userId, long bookId) {
        String sql = "DELETE FROM carts WHERE user_id = ? AND book_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE,
                    "Error removing cart item - User: " + userId + ", Book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Clears all items from a user's cart.
     *
     * @param userId The ID of the user whose cart to clear
     * @return true if cart was cleared successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean clearCart(long userId) {
        String sql = "DELETE FROM carts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error clearing cart for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    // ===== QUERY OPERATIONS =====

    /**
     * Retrieves all items in a user's cart with book details.
     *
     * @param userId The ID of the user
     * @return List of cart items with book information
     * @throws RuntimeException if database error occurs
     */
    public List<CartItem> getCartByUser(long userId) {
        String sql = "SELECT c.id, c.user_id, c.book_id, c.quantity, c.created_at, "
                + "b.title, b.price "
                + "FROM carts c JOIN books b ON c.book_id = b.id WHERE c.user_id = ? ORDER BY c.created_at DESC";

        List<CartItem> cartItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cartItems.add(mapCartItemWithBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting cart for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return cartItems;
    }

    /**
     * Gets a specific cart item by ID.
     *
     * @param cartId The ID of the cart item
     * @return Optional containing CartItem if found, empty otherwise
     */
    public Optional<CartItem> getCartItemById(long cartId) {
        String sql = "SELECT c.id, c.user_id, c.book_id, c.quantity, c.created_at, "
                + "b.title, b.price "
                + "FROM carts c JOIN books b ON c.book_id = b.id WHERE c.id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCartItemWithBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting cart item: " + cartId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return Optional.empty();
    }

    /**
     * Gets a specific cart item by user and book.
     *
     * @param userId The ID of the user
     * @param bookId The ID of the book
     * @return Optional containing CartItem if found, empty otherwise
     */
    public Optional<CartItem> getCartItem(long userId, long bookId) {
        String sql = "SELECT c.id, c.user_id, c.book_id, c.quantity, c.created_at, "
                + "b.title, b.price "
                + "FROM carts c JOIN books b ON c.book_id = b.id WHERE c.user_id = ? AND c.book_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCartItemWithBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting cart item - User: " + userId + ", Book: " + bookId,
                    e);
            throw new RuntimeException("Database error occurred", e);
        }
        return Optional.empty();
    }

    // ===== UTILITY OPERATIONS =====

    /**
     * Counts the number of items in a user's cart.
     *
     * @param userId The ID of the user
     * @return Number of different items in the cart
     */
    public int getCartItemCount(long userId) {
        String sql = "SELECT COUNT(*) FROM carts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error counting cart items for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    /**
     * Calculates the total quantity of all items in a user's cart.
     *
     * @param userId The ID of the user
     * @return Total quantity of all items
     */
    public int getTotalCartQuantity(long userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM carts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error calculating total cart quantity for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    /**
     * Calculates the total value of all items in a user's cart.
     *
     * @param userId The ID of the user
     * @return Total value of all cart items
     */
    public BigDecimal getCartTotal(long userId) {
        String sql = "SELECT COALESCE(SUM(c.quantity * b.price), 0) "
                + "FROM carts c JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error calculating cart total for user: " + userId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Checks if a book is already in the user's cart.
     *
     * @param userId The ID of the user
     * @param bookId The ID of the book
     * @return true if book is in cart, false otherwise
     */
    public boolean isBookInCart(long userId, long bookId) {
        String sql = "SELECT 1 FROM carts WHERE user_id = ? AND book_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,
                    "Error checking if book is in cart - User: " + userId + ", Book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    // ===== HELPER METHODS =====

    /**
     * Maps a ResultSet row to a CartItem object with book details.
     *
     * @param rs The ResultSet containing cart and book data
     * @return CartItem object populated with data from ResultSet
     * @throws SQLException if database error occurs
     */
    private CartItem mapCartItemWithBook(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getLong("id"));
        cartItem.setUserId(rs.getLong("user_id"));
        cartItem.setBookId(rs.getLong("book_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setCreatedAt(rs.getTimestamp("created_at"));

        // Transient fields from books table
        cartItem.setTitle(rs.getString("title"));
        cartItem.setPrice(rs.getDouble("price"));

        return cartItem;
    }
}
