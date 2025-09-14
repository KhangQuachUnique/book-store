package dao;

import model.Review;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewDao {
    public static List<Review> getReviewsByBookId(int bookId) {
        String sql = "SELECT r.id, r.user_id, r.book_id, r.rating, u.name AS user_name, u.avatar_url, " +
                "r.comment, r.like_count, r.created_at " +
                "FROM reviews r JOIN users u ON r.user_id = u.id " +
                "WHERE r.book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (rs.next()) {
                    reviews.add(mapRow(rs));
                }
                return reviews;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static Review mapRow(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setBookId(rs.getInt("book_id"));
        review.setRating(rs.getDouble("rating"));
        review.setUsername(rs.getString("user_name"));
        review.setComment(rs.getString("comment"));
        review.setLikeCount(rs.getInt("like_count"));
        review.setAvatarUrl(rs.getString("avatar_url"));
        review.setDate(rs.getString("created_at"));
        review.calculateStars();
        return review;
    }
}
