package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.BookReview;
import model.Review;
import model.ReviewShow;
import util.JPAUtil;

public class BookReviewDao {
    private static final Logger log = Logger.getLogger(BookReviewDao.class.getName());

    public static BookReview getReviewsByBookId(long bookId, long currentUserId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Lấy tất cả review cho book này
            TypedQuery<Review> query = em.createQuery(
                "SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.book.id = :bookId ORDER BY r.createdAt DESC", Review.class);
            query.setParameter("bookId", bookId);
            
            List<Review> reviews = query.getResultList();
            
            // Chuyển đổi sang ReviewShow với logic like
            List<ReviewShow> reviewShows = new ArrayList<>();
            for (Review review : reviews) {
                ReviewShow reviewShow = new ReviewShow();
                reviewShow.setId(review.getId().intValue());
                reviewShow.setUserId(review.getUser().getId().intValue());
                reviewShow.setRating(review.getRating().doubleValue());
                reviewShow.setUsername(review.getUser().getName());
                reviewShow.setComment(review.getComment());
                reviewShow.setAvatarUrl(review.getUser().getAvatarUrl());
                reviewShow.setDate(review.getCreatedAt());
                
                // Đếm likes cho review này
                TypedQuery<Long> likeCountQuery = em.createQuery(
                    "SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId", Long.class);
                likeCountQuery.setParameter("reviewId", review.getId());
                Long likeCount = likeCountQuery.getSingleResult();
                reviewShow.setLikeCount(likeCount.intValue());
                
                // Kiểm tra user hiện tại đã like chưa
                if (currentUserId > 0) {
                    TypedQuery<Long> userLikeQuery = em.createQuery(
                        "SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId AND rl.user.id = :userId", Long.class);
                    userLikeQuery.setParameter("reviewId", review.getId());
                    userLikeQuery.setParameter("userId", currentUserId);
                    reviewShow.setLikedByCurrentUser(userLikeQuery.getSingleResult() > 0);
                }
                
                reviewShow.calculateStars();
                reviewShows.add(reviewShow);
            }
            
            // Tính average rating
            double averageRating = reviewShows.stream()
                    .mapToDouble(ReviewShow::getRating)
                    .average()
                    .orElse(0.0);
            
            BookReview bookReview = new BookReview();
            bookReview.setAverageRating(averageRating);
            bookReview.setTotalReviews(reviewShows.size());
            bookReview.calculateStars();
            
            return bookReview;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy reviews cho book " + bookId, e);
            return null;
        } finally {
            em.close();
        }
    }

    public static boolean likeReview(int reviewId, int userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            
            // Kiểm tra đã like chưa
            TypedQuery<Long> checkQuery = em.createQuery(
                "SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId AND rl.user.id = :userId", Long.class);
            checkQuery.setParameter("reviewId", (long) reviewId);
            checkQuery.setParameter("userId", (long) userId);
            
            if (checkQuery.getSingleResult() > 0) {
                em.getTransaction().rollback();
                return false; // Đã like rồi
            }
            
            // Tạo ReviewLike mới - cần tạo entity ReviewLike
            em.createNativeQuery("INSERT INTO review_likes (review_id, user_id) VALUES (?, ?)")
              .setParameter(1, reviewId)
              .setParameter(2, userId)
              .executeUpdate();
            
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Lỗi khi like review " + reviewId + " từ user " + userId, e);
            return false;
        } finally {
            em.close();
        }
    }

    public static boolean unlikeReview(int reviewId, int userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            
            int rowsAffected = em.createNativeQuery("DELETE FROM review_likes WHERE review_id = ? AND user_id = ?")
                .setParameter(1, reviewId)
                .setParameter(2, userId)
                .executeUpdate();
            
            em.getTransaction().commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Lỗi khi unlike review " + reviewId + " từ user " + userId, e);
            return false;
        } finally {
            em.close();
        }
    }
}
