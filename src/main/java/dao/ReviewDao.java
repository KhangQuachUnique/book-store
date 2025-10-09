package dao;

import jakarta.persistence.*;
import model.LikeReview;
import model.Review;
import util.JPAUtil;

import java.util.List;

public class ReviewDao {
    private final EntityManagerFactory emf;

    public ReviewDao() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    public List<Review> getReviewsByBookId(Long bookId, Long currentUserId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = """
                SELECT DISTINCT r
                FROM Review r
                LEFT JOIN FETCH r.user u
                LEFT JOIN FETCH r.likes l
                WHERE r.book.id = :bookId
                ORDER BY r.createdAt DESC
            """;

            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            query.setParameter("bookId", bookId);

            List<Review> reviews = query.getResultList();

            for (Review r : reviews) {
                List<LikeReview> likes = r.getLikes();

                long likeCount = r.getLikes() == null ? 0 : r.getLikes().size();
                boolean liked = (likes != null) && likes.stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId));
                r.setLikeCount(likeCount);
                r.setLikedByCurrentUser(liked);
            }

            return reviews;
        } finally {
            em.close();
        }
    }

    public boolean likeReview(Long reviewId, Long userId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Review review = em.find(Review.class, reviewId);
            if (review == null) {
                tx.rollback();
                return false;
            }

            LikeReview likeReview = new LikeReview();
            likeReview.setReview(review);
            likeReview.setUser(em.getReference(model.User.class, userId));

            em.persist(likeReview);
            tx.commit();
            return true;

        } catch (PersistenceException e) {
            // Nếu vi phạm UNIQUE constraint thì DB sẽ ném lỗi ở đây
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                System.out.println("User đã like review này rồi!");
                // Không rollback transaction ở đây, vì commit chưa diễn ra
                return false;
            }
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean dislikeReview(Long reviewId, Long userId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Tìm review
            Review review = em.find(Review.class, reviewId);
            if (review == null) {
                tx.rollback();
                System.out.println("Review không tồn tại");
                return false;
            }

            // Tìm bản ghi LikeReview tương ứng
            TypedQuery<LikeReview> query = em.createQuery(
                    "SELECT lr FROM LikeReview lr WHERE lr.review.id = :reviewId AND lr.user.id = :userId",
                    LikeReview.class
            );
            query.setParameter("reviewId", reviewId);
            query.setParameter("userId", userId);

            List<LikeReview> results = query.getResultList();

            if (results.isEmpty()) {
                // chưa like, không có gì để dislike
                tx.rollback();
                System.out.println("User chưa like review này, không thể dislike.");
                return false;
            }

            // có like -> xóa
            LikeReview likeReview = results.get(0);
            em.remove(likeReview);

            tx.commit();
            System.out.println("Đã dislike (bỏ like) review thành công!");
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;

        } finally {
            em.close();
        }
    }
}
