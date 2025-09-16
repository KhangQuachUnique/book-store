package dao;

import model.BookReview;
import model.Review;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookReviewDao {
    public static BookReview getReviewsByBookId(int bookId) {
        String sql = "SELECT r.id, r.book_id, r.user_id, u.name AS username, u.avatar_url, " +
                    "r.rating, r.comment, r.like_count, r.created_at " +
                "FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (rs.next()) {
                    reviews.add(mapRow(rs));
                }
                double averageRating = reviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0);
                BookReview bookReview = new BookReview();
                bookReview.setBookId(bookId);
                bookReview.setReviews(reviews);
                bookReview.setAverageRating(averageRating);
                bookReview.setTotalReviews(reviews.size());
                bookReview.calculateStars();
                return bookReview;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean likeReview(int reviewId) {
        String sql = "UPDATE reviews SET like_count = like_count + 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unlikeReview(int reviewId) {
        String sql = "UPDATE reviews SET like_count = GREATEST(like_count - 1, 0) WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Review mapRow(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setRating(rs.getDouble("rating"));
        review.setUsername(rs.getString("username"));
        review.setComment(rs.getString("comment"));
        review.setLikeCount(rs.getInt("like_count"));
        review.setAvatarUrl(rs.getString("avatar_url"));
        review.setDate(rs.getTimestamp("created_at"));
        review.calculateStars();
        return review;
    }
}
