//package dao;
//
//import model.Book;
//import model.ViewedProductItem;
//import util.DBConnection;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ViewHistoryDao {
//
//    /**
//     * Lấy lịch sử xem sách theo userId
//     */
//    public List<ViewedProductItem> getHistoryByUserId(Long userId) {
//        List<ViewedProductItem> list = new ArrayList<>();
//        String sql = """
//            SELECT v.id, v.viewed_at,
//                   b.id AS book_id, b.title, b.author, b.publisher,
//                   b.category_id, b.thumbnail_url, b.description, b.stock,
//                   b.publish_year, b.pages, b.rating_average,
//                   b.price, b.original_price, b.discount_rate, b.created_at
//            FROM viewed v
//            JOIN books b ON v.book_id = b.id
//            WHERE v.user_id = ?
//            ORDER BY v.viewed_at DESC
//        """;
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setLong(1, userId);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                // Map Book
//                Book book = new Book();
//                book.setId(rs.getInt("book_id"));
//                book.setTitle(rs.getString("title"));
//                book.setAuthor(rs.getString("author"));
//                book.setPublisher(rs.getString("publisher"));
//                book.setCategoryId(rs.getInt("category_id"));
//                book.setThumbnailUrl(rs.getString("thumbnail_url"));
//                book.setDescription(rs.getString("description"));
//                book.setStock(rs.getInt("stock"));
//                book.setPublishYear(rs.getInt("publish_year"));
//                book.setPages(rs.getInt("pages"));
//                book.setRating(rs.getDouble("rating_average"));   // ✅ map vào field rating
//                book.setPrice(rs.getDouble("price"));
//                book.setOriginalPrice(rs.getDouble("original_price"));
//                book.setDiscount_rate(rs.getInt("discount_rate")); // ✅ map vào field discount_rate
//                book.setCreatedAt(rs.getTimestamp("created_at"));
//                book.calculateStars();
//                // Map ViewHistoryItem
//                ViewedProductItem item = new ViewedProductItem(
//                        rs.getLong("id"),
//                        book,
//                        rs.getTimestamp("viewed_at")
//                );
//
//                list.add(item);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    /**
//     * Ghi lại lịch sử xem sách
//     */
//    public void addHistory(Long userId, Long bookId) {
//        String sql = "INSERT INTO viewed (user_id, book_id) VALUES (?, ?)";
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setLong(1, userId);
//            ps.setLong(2, bookId);
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
