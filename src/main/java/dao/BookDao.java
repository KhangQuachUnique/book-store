package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Book;
import model.Category;
import util.DBConnection;

/**
 * Data Access Object for managing books in the database.
 */
public class BookDao {
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(BookDao.class.getName());
    private static final int PAGE_SIZE = 20; // Limit to 20 books per page

    /**
     * Retrieves all books with pagination.
     *
     * @param page The page number (1-based).
     * @return List of books for the specified page.
     * @throws SQLException If a database error occurs.
     */
    public static List<Book> getAllBooks(int page) throws SQLException {
        String sql = "SELECT * FROM books ORDER BY id LIMIT ? OFFSET ?";
        return getBooksByQuery(sql, page, null);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The book ID.
     * @return The Book object or null if not found.
     * @throws SQLException If a database error occurs.
     */
    public static Book getBookById(long id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        }
        return null;
    }

    /**
     * Adds a new book to the database.
     *
     * @param book The Book object to add.
     * @return True if successful, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    public static boolean addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, category_id, stock, original_price, discount_rate, "
                +
                "thumbnail_url, description, publish_year, pages, rating_average, price, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setInt(4, book.getCategoryId());
            ps.setInt(5, book.getStock());
            ps.setDouble(6, book.getOriginalPrice());
            ps.setInt(7, book.getDiscount_rate());
            ps.setString(8, book.getThumbnailUrl());
            ps.setString(9, book.getDescription());
            ps.setObject(10, book.getPublishYear(), Types.INTEGER);
            ps.setObject(11, book.getPages(), Types.INTEGER);
            ps.setDouble(12, book.getRating());
            ps.setDouble(13, book.getPrice());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing book.
     *
     * @param book The Book object with updated data.
     * @return True if successful, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    public static boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, category_id = ?, stock = ?, " +
                "original_price = ?, discount_rate = ?, thumbnail_url = ?, description = ?, publish_year = ?, " +
                "pages = ?, rating_average = ?, price = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            setBookParameters(ps, book);
            ps.setLong(14, book.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id The book ID.
     * @return True if successful, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    public static boolean deleteBook(long id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Filters books by title, publish year, and categories (include/exclude).
     *
     * @param title             The title to search (partial match).
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include (AND logic).
     * @param excludeCategories Categories to exclude.
     * @param page              The page number.
     * @return List of matching books.
     * @throws SQLException If a database error occurs.
     */
    public static List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
            List<Long> excludeCategories, int page) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            sql.append(" AND title ILIKE ?");
            params.add("%" + title + "%");
        }
        if (publishYear != null) {
            sql.append(" AND publish_year = ?");
            params.add(publishYear);
        }
        if (includeCategories != null && !includeCategories.isEmpty()) {
            sql.append(" AND category_id IN (").append("?,".repeat(includeCategories.size()))
                    .deleteCharAt(sql.length() - 1).append(")");
            params.addAll(includeCategories);
        }
        if (excludeCategories != null && !excludeCategories.isEmpty()) {
            sql.append(" AND category_id NOT IN (").append("?,".repeat(excludeCategories.size()))
                    .deleteCharAt(sql.length() - 1).append(")");
            params.addAll(excludeCategories);
        }
        sql.append(" ORDER BY id LIMIT ? OFFSET ?");
        params.add(PAGE_SIZE);
        params.add((page - 1) * PAGE_SIZE);

        return getBooksByQuery(sql.toString(), page, params.toArray());
    }

    /**
     * Counts total books for pagination or filtering.
     *
     * @param title             The title to search.
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include.
     * @param excludeCategories Categories to exclude.
     * @return Total number of matching books.
     * @throws SQLException If a database error occurs.
     */
    public static long countBooks(String title, Integer publishYear, List<Long> includeCategories,
            List<Long> excludeCategories) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            sql.append(" AND title ILIKE ?");
            params.add("%" + title + "%");
        }
        if (publishYear != null) {
            sql.append(" AND publish_year = ?");
            params.add(publishYear);
        }
        if (includeCategories != null && !includeCategories.isEmpty()) {
            sql.append(" AND category_id IN (").append("?,".repeat(includeCategories.size()))
                    .deleteCharAt(sql.length() - 1).append(")");
            params.addAll(includeCategories);
        }
        if (excludeCategories != null && !excludeCategories.isEmpty()) {
            sql.append(" AND category_id NOT IN (").append("?,".repeat(excludeCategories.size()))
                    .deleteCharAt(sql.length() - 1).append(")");
            params.addAll(excludeCategories);
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return 0;
    }

    /**
     * Retrieves all categories for filtering.
     *
     * @return List of categories.
     * @throws SQLException If a database error occurs.
     */
    public static List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getLong("parent_id"));
                category.setIsLeaf(rs.getBoolean("is_leaf"));
                categories.add(category);
            }
        }
        return categories;
    }

    /**
     * Logs CSV import data to import_logs table.
     *
     * @param tableName    The table name (e.g., "books").
     * @param importedData JSON representation of imported data.
     * @return True if successful, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    public static boolean logImport(String tableName, String importedData) throws SQLException {
        String sql = "INSERT INTO import_logs (table_name, imported_data, imported_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, importedData);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Maps a ResultSet row to a Book object.
     *
     * @param rs The ResultSet.
     * @return The mapped Book object.
     * @throws SQLException If a database error occurs.
     */
    private static Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setStock(rs.getInt("stock"));
        book.setOriginalPrice(rs.getDouble("original_price"));
        book.setDiscount_rate(rs.getInt("discount_rate"));
        book.setThumbnailUrl(rs.getString("thumbnail_url"));
        book.setDescription(rs.getString("description"));
        book.setPublishYear(rs.getInt("publish_year"));
        book.setPages(rs.getInt("pages"));
        book.setRating(rs.getDouble("rating_average"));
        book.setPrice(rs.getDouble("price"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        return book;
    }

    /**
     * Sets PreparedStatement parameters for add/update book queries.
     *
     * @param ps   The PreparedStatement.
     * @param book The Book object.
     * @throws SQLException If a database error occurs.
     */
    private static void setBookParameters(PreparedStatement ps, Book book) throws SQLException {
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getPublisher());
        ps.setInt(4, book.getCategoryId());
        ps.setInt(5, book.getStock());
        ps.setDouble(6, book.getOriginalPrice());
        ps.setInt(7, book.getDiscount_rate());
        ps.setString(8, book.getThumbnailUrl());
        ps.setString(9, book.getDescription());
        ps.setObject(10, book.getPublishYear(), Types.INTEGER);
        ps.setObject(11, book.getPages(), Types.INTEGER);
        ps.setDouble(12, book.getRating());
        ps.setDouble(13, book.getPrice());
    }

    /**
     * Executes a query to retrieve books with dynamic parameters.
     *
     * @param sql    The SQL query.
     * @param page   The page number.
     * @param params Query parameters.
     * @return List of books.
     * @throws SQLException If a database error occurs.
     */
    private static List<Book> getBooksByQuery(String sql, int page, Object[] params) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            } else {
                ps.setInt(1, PAGE_SIZE);
                ps.setInt(2, (page - 1) * PAGE_SIZE);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }
}
