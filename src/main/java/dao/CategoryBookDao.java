package dao;

import model.Book;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryBookDao {
    public static List<Book> getAllBook() {
        String sql = "SELECT * FROM books LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Book> books = new java.util.ArrayList<>();
            while (rs.next()) {
                Book b = mapResultSetToBook(rs);
                books.add(b);
            }
            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Book> getBooksByCategoryId(int categoryId) {
        String sql = "SELECT * FROM books WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
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
        return b;
    }
}
