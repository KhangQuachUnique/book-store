package dao;

import model.Book;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WishListDao {
    public static List<Book> getWishListBooks(int userId) {
        // This is a stub implementation. Replace with actual database access code.
        // For example, you might query a database to get the wishlist books for the given userId.
        String sql = " SELECT b.* FROM wishlists w JOIN books b ON w.book_id = b.id WHERE w.user_id = ? ";

        List<Book> books = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPrice(rs.getDouble("price"));
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
                book.setCreatedAt(rs.getTimestamp("created_at"));

                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public static boolean addBookToWishList(int userId, int bookId) {
        String sql = " INSERT INTO wishlists (user_id, book_id) VALUES (?, ?) ";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteBookFromWishList(int userId, int bookId) {
        String sql = " DELETE FROM wishlists WHERE user_id = ? AND book_id = ? ";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}