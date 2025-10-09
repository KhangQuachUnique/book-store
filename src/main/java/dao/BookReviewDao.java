//package dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import model.BookReview;
//import model.ReviewShow;
//import util.DBConnection;
//
//public class BookReviewDao {
//    public static BookReview getReviewsByBookId(long bookId, long currentUserId) {
//        String sql = "SELECT r.id, " +
//                "r.book_id, " +
//                "r.user_id, " +
//                "u.name AS username, " +
//                "u.avatar_url, " +
//                "r.rating, " +
//                "r.comment, " +
//                "r.created_at, " +
//                "COUNT(rl_all.id) AS like_count, " +
//                "CASE WHEN rl_user.user_id IS NOT NULL THEN TRUE ELSE FALSE END AS liked_by_current_user " +
//                "FROM reviews r " +
//                "JOIN users u ON r.user_id = u.id " +
//                "LEFT JOIN review_likes rl_all ON r.id = rl_all.review_id " +
//                "LEFT JOIN review_likes rl_user ON r.id = rl_user.review_id AND rl_user.user_id = ? " +
//                "WHERE r.book_id = ? " +
//                "GROUP BY r.id, r.book_id, r.user_id, u.name, u.avatar_url, r.rating, r.comment, r.created_at, rl_user.user_id "
//                +
//                "ORDER BY r.created_at DESC";
//
//        try (Connection conn = DBConnection.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setLong(1, currentUserId);
//            ps.setLong(2, bookId);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                List<ReviewShow> reviewShows = new ArrayList<>();
//                while (rs.next()) {
//                    reviewShows.add(mapRow(rs));
//                }
//                double averageRating = reviewShows.stream()
//                        .mapToDouble(ReviewShow::getRating)
//                        .average()
//                        .orElse(0.0);
//                BookReview bookReview = new BookReview();
//                bookReview.setBookId(bookId);
//                bookReview.setReviewShows(reviewShows);
//                bookReview.setAverageRating(averageRating);
//                bookReview.setTotalReviews(reviewShows.size());
//                bookReview.calculateStars();
//                return bookReview;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static boolean likeReview(int reviewId, int userId) {
//        String sql = "INSERT INTO review_likes (review_id, user_id) VALUES (?, ?)";
//        try (Connection conn = DBConnection.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, reviewId);
//            ps.setInt(2, userId);
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public static boolean unlikeReview(int reviewId, int userId) {
//        String sql = "DELETE FROM review_likes WHERE review_id=? AND user_id=?";
//        try (Connection conn = DBConnection.getConnection();
//                PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, reviewId);
//            ps.setInt(2, userId);
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private static ReviewShow mapRow(ResultSet rs) throws SQLException {
//        ReviewShow reviewShow = new ReviewShow();
//        reviewShow.setId(rs.getInt("id"));
//        reviewShow.setUserId(rs.getInt("user_id"));
//        reviewShow.setRating(rs.getDouble("rating"));
//        reviewShow.setUsername(rs.getString("username"));
//        reviewShow.setComment(rs.getString("comment"));
//        reviewShow.setLikeCount(rs.getInt("like_count"));
//        reviewShow.setAvatarUrl(rs.getString("avatar_url"));
//        reviewShow.setDate(rs.getTimestamp("created_at"));
//        reviewShow.setLikedByCurrentUser(rs.getBoolean("liked_by_current_user"));
//        reviewShow.calculateStars();
//        return reviewShow;
//    }
//}
