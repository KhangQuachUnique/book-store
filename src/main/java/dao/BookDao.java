package dao;

import model.Book;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public static List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books where id = 450147";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book b = mapResultSetToBook(rs);
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, category_id, stock, original_price, discount_rate, thumbnail_url, description, publish_year, pages, rating, price, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setInt(4, book.getCategoryId());
            ps.setInt(5, book.getStock());
            ps.setDouble(6, book.getOriginalPrice());
            ps.setInt(7, book.getDiscount_rate());
            ps.setString(8, book.getthumbnailUrl());
            ps.setString(9, book.getDescription());
            ps.setInt(10, book.getPublishYear());
            ps.setInt(11, book.getPages());
            ps.setDouble(12, book.getRating());
            ps.setDouble(13, book.getPrice());
            ps.setTimestamp(14, book.getCreatedAt());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
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
        b.setthumbnailUrl(rs.getString("thumbnail_url"));
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
