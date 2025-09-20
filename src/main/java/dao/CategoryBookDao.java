package dao;

import model.Book;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryBookDao {
    private static final int BOOKS_PER_PAGE = 40;

    public static List<Book> getAllBook(int page) {
        String sql = "SELECT * FROM books LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, BOOKS_PER_PAGE);
            ps.setInt(2, (page - 1) * BOOKS_PER_PAGE);

            try (ResultSet rs = ps.executeQuery()) {
                List<Book> books = new java.util.ArrayList<>();
                while (rs.next()) {
                    Book b = mapResultSetToBook(rs);
                    books.add(b);
                }
                return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTotalBooks() {
        String sql = "SELECT COUNT(*) as total FROM books";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTotalPages() {
        int totalBooks = getTotalBooks();
        return (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);
    }

    public static List<Book> getBooksByCategoryId(int categoryId, int page) {
        String sql = "SELECT * FROM books WHERE category_id = ? LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, BOOKS_PER_PAGE);
            ps.setInt(3, (page - 1) * BOOKS_PER_PAGE);

            try (var rs = ps.executeQuery()) {
                List<Book> books = new java.util.ArrayList<>();
                while (rs.next()) {
                    Book b = mapResultSetToBook(rs);
                    books.add(b);
                }
                return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTotalBooksByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) as total FROM books WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTotalPagesByCategory(int categoryId) {
        int totalBooks = getTotalBooksByCategory(categoryId);
        return (int) Math.ceil((double) totalBooks / BOOKS_PER_PAGE);
    }

    private static Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setStock(rs.getInt("stock"));
        b.setAuthor(rs.getString("author"));
        b.setPublisher(rs.getString("publisher"));
        b.setThumbnailUrl(rs.getString("thumbnail_url"));
        b.setDescription(rs.getString("description"));
        b.setPublishYear(rs.getInt("publish_year"));
        b.setPages(rs.getInt("pages"));
        b.setRating(rs.getDouble("rating_average"));
        b.setPrice(rs.getDouble("price"));
        b.setOriginalPrice(rs.getDouble("original_price"));
        b.setDiscount_rate(rs.getInt("discount_rate"));
        b.setCategoryId(rs.getInt("category_id"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.calculateStars();
        return b;
    }
}
