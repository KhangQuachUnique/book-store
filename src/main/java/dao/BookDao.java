package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;
import util.DBConnection;

/**
 * Data Access Object for Book entity operations. Provides methods for book CRUD operations, search,
 * filtering, and inventory management.
 *
 * @author BookStore Team
 * @version 1.0
 */
public class BookDao {

    private static final Logger log = Logger.getLogger(BookDao.class.getName());
    private static final int PAGE_SIZE = 20;

    // ===== CORE CRUD OPERATIONS =====

    /**
     * Creates a new book in the database.
     *
     * @param book The book object to save
     * @return true if book was saved successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean addBook(Book book) {
        String sql =
                "INSERT INTO books (title, author, publisher, category_id, stock, original_price, "
                        + "discount_rate, thumbnail_url, description, publish_year, pages, rating_average, "
                        + "price, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setLong(4, book.getCategoryId());
            ps.setInt(5, book.getStock());
            ps.setBigDecimal(6, book.getOriginalPrice());
            ps.setInt(7, book.getDiscountRate());
            ps.setString(8, book.getThumbnailUrl());
            ps.setString(9, book.getDescription());
            ps.setInt(10, book.getPublishYear());
            ps.setInt(11, book.getPages());
            ps.setBigDecimal(12, book.getRatingAverage());
            ps.setBigDecimal(13, book.getPrice());
            ps.setTimestamp(14, book.getCreatedAt());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error adding book: " + book.getTitle(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Finds a book by its ID.
     *
     * @param id The book ID to search for
     * @return Optional containing Book if found, empty otherwise
     */
    public Optional<Book> getBookById(long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding book by ID: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing book's information.
     *
     * @param book The book object with updated information
     * @return true if book was updated successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, category_id = ?, "
                + "stock = ?, original_price = ?, discount_rate = ?, thumbnail_url = ?, "
                + "description = ?, publish_year = ?, pages = ?, rating_average = ?, "
                + "price = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setLong(4, book.getCategoryId());
            ps.setInt(5, book.getStock());
            ps.setBigDecimal(6, book.getOriginalPrice());
            ps.setInt(7, book.getDiscountRate());
            ps.setString(8, book.getThumbnailUrl());
            ps.setString(9, book.getDescription());
            ps.setInt(10, book.getPublishYear());
            ps.setInt(11, book.getPages());
            ps.setBigDecimal(12, book.getRatingAverage());
            ps.setBigDecimal(13, book.getPrice());
            ps.setLong(14, book.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating book: " + book.getId(), e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param id The ID of the book to delete
     * @return true if book was deleted successfully, false otherwise
     * @throws RuntimeException if database error occurs
     */
    public boolean deleteBook(long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting book: " + id, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    // ===== LIST & SEARCH OPERATIONS =====

    /**
     * Gets all books with pagination.
     *
     * @param page The page number (1-based)
     * @return List of books for the requested page
     */
    public List<Book> getAllBooks(int page) {
        String sql = "SELECT * FROM books ORDER BY id LIMIT ? OFFSET ?";
        return getBooksWithPagination(sql, page);
    }

    /**
     * Gets all books without pagination.
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books ORDER BY id";
        return getBooksWithoutPagination(sql);
    }

    /**
     * Gets books by category with pagination.
     *
     * @param categoryId The category ID to filter by
     * @param page The page number (1-based)
     * @return List of books in the specified category
     */
    public List<Book> getBooksByCategory(long categoryId, int page) {
        String sql = "SELECT * FROM books WHERE category_id = ? ORDER BY id LIMIT ? OFFSET ?";
        return getBooksWithPagination(sql, page, categoryId);
    }

    /**
     * Searches books by title, author, or description.
     *
     * @param query The search query
     * @param page The page number (1-based)
     * @return List of matching books for the requested page
     */
    public List<Book> searchBooks(String query, int page) {
        String sql =
                "SELECT * FROM books WHERE title ILIKE ? OR author ILIKE ? OR description ILIKE ? "
                        + "ORDER BY id LIMIT ? OFFSET ?";

        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            ps.setInt(4, PAGE_SIZE);
            ps.setInt(5, (page - 1) * PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error searching books with query: " + query, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return books;
    }

    /**
     * Gets featured books (high rating, good stock).
     *
     * @param limit The maximum number of books to return
     * @return List of featured books
     */
    public List<Book> getFeaturedBooks(int limit) {
        String sql = "SELECT * FROM books WHERE rating_average >= 4.0 AND stock > 0 "
                + "ORDER BY rating_average DESC, stock DESC LIMIT ?";

        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting featured books", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return books;
    }

    /**
     * Gets books with low stock.
     *
     * @param threshold The stock threshold below which books are considered low stock
     * @return List of books with low stock
     */
    public List<Book> getLowStockBooks(int threshold) {
        String sql = "SELECT * FROM books WHERE stock <= ? ORDER BY stock ASC";

        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting low stock books", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return books;
    }

    // ===== INVENTORY MANAGEMENT =====

    /**
     * Updates book stock quantity.
     *
     * @param bookId The ID of the book
     * @param newStock The new stock quantity
     * @return true if stock was updated successfully, false otherwise
     */
    public boolean updateStock(long bookId, int newStock) {
        String sql = "UPDATE books SET stock = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStock);
            ps.setLong(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating stock for book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Decreases book stock by specified quantity.
     *
     * @param bookId The ID of the book
     * @param quantity The quantity to decrease
     * @return true if stock was decreased successfully, false otherwise
     */
    public boolean decreaseStock(long bookId, int quantity) {
        String sql = "UPDATE books SET stock = stock - ?, updated_at = CURRENT_TIMESTAMP "
                + "WHERE id = ? AND stock >= ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, bookId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error decreasing stock for book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    /**
     * Increases book stock by specified quantity.
     *
     * @param bookId The ID of the book
     * @param quantity The quantity to increase
     * @return true if stock was increased successfully, false otherwise
     */
    public boolean increaseStock(long bookId, int quantity) {
        String sql =
                "UPDATE books SET stock = stock + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error increasing stock for book: " + bookId, e);
            throw new RuntimeException("Database error occurred", e);
        }
    }

    // ===== COUNTING OPERATIONS =====

    /**
     * Counts total number of books.
     *
     * @return Total number of books
     */
    public long countBooks() {
        String sql = "SELECT COUNT(*) FROM books";
        return getCount(sql);
    }

    /**
     * Counts books in a specific category.
     *
     * @param categoryId The category ID
     * @return Number of books in the category
     */
    public long countBooksByCategory(long categoryId) {
        String sql = "SELECT COUNT(*) FROM books WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error counting books by category: " + categoryId, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    /**
     * Counts books matching search query.
     *
     * @param query The search query
     * @return Number of matching books
     */
    public long countSearchResults(String query) {
        String sql =
                "SELECT COUNT(*) FROM books WHERE title ILIKE ? OR author ILIKE ? OR description ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error counting search results for: " + query, e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    // ===== HELPER METHODS =====

    /**
     * Helper method to execute paginated book queries.
     *
     * @param sql The SQL query with LIMIT and OFFSET
     * @param page The page number (1-based)
     * @param params Additional parameters for the query
     * @return List of books for the requested page
     */
    private List<Book> getBooksWithPagination(String sql, int page, Object... params) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set additional parameters first
            int paramIndex = 1;
            for (Object param : params) {
                if (param instanceof Long) {
                    ps.setLong(paramIndex++, (Long) param);
                } else if (param instanceof String) {
                    ps.setString(paramIndex++, (String) param);
                }
            }

            // Set pagination parameters
            ps.setInt(paramIndex++, PAGE_SIZE);
            ps.setInt(paramIndex, (page - 1) * PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error executing paginated book query", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return books;
    }

    /**
     * Helper method to execute book queries without pagination.
     *
     * @param sql The SQL query
     * @return List of all matching books
     */
    private List<Book> getBooksWithoutPagination(String sql) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error executing book query", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return books;
    }

    /**
     * Helper method to get count from database.
     *
     * @param sql The count SQL query
     * @return The count result
     */
    private long getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error getting count", e);
            throw new RuntimeException("Database error occurred", e);
        }
        return 0;
    }

    /**
     * Maps a ResultSet row to a Book object.
     *
     * @param rs The ResultSet containing book data
     * @return Book object populated with data from ResultSet
     * @throws SQLException if database error occurs
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setCategoryId(rs.getLong("category_id"));
        book.setStock(rs.getInt("stock"));
        book.setOriginalPrice(rs.getBigDecimal("original_price"));
        book.setDiscountRate(rs.getInt("discount_rate"));
        book.setThumbnailUrl(rs.getString("thumbnail_url"));
        book.setDescription(rs.getString("description"));
        book.setPublishYear(rs.getInt("publish_year"));
        book.setPages(rs.getInt("pages"));
        book.setRatingAverage(rs.getBigDecimal("rating_average"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setCreatedAt(rs.getTimestamp("created_at"));

        // Set review count if available
        try {
            book.setReviewCount(rs.getInt("review_count"));
        } catch (SQLException e) {
            // Column might not exist in all queries, set default
            book.setReviewCount(0);
        }

        return book;
    }
}
