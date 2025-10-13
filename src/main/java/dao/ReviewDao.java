package dao;

import jakarta.persistence.*;
import model.Book;
import model.LikeReview;
import model.Review;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /** Check if user already reviewed this book */
    public boolean hasUserReviewedBook(Long userId, Long bookId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Review r WHERE r.user.id = :uid AND r.book.id = :bid",
                            Long.class)
                    .setParameter("uid", userId)
                    .setParameter("bid", bookId)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    /** Get ids of books (as Integer) that the user already reviewed from a given list */
    public Set<Integer> getReviewedBookIds(Long userId, List<Integer> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) return java.util.Collections.emptySet();
        EntityManager em = emf.createEntityManager();
        try {
            List<Integer> result = em.createQuery(
                            "SELECT r.book.id FROM Review r WHERE r.user.id = :uid AND r.book.id IN :ids",
                            Integer.class)
                    .setParameter("uid", userId)
                    .setParameter("ids", bookIds)
                    .getResultList();
            return result.stream().collect(Collectors.toSet());
        } finally {
            em.close();
        }
    }

    /**
     * Create a review once. If already exists, return false. Also update the book's averageRating.
     */
    public boolean addOrUpdateReview(Long userId, Long bookId, Integer rating, String comment) {
        // Enforce single review: do not update if exists
        if (hasUserReviewedBook(userId, bookId)) {
            return false;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Book bookRef = em.getReference(Book.class, bookId);
            Review review = new Review();
            review.setUser(em.getReference(model.User.class, userId));
            review.setBook(bookRef);
            review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            review.setRating(rating);
            review.setComment(comment);
            em.persist(review);

            // Recalculate average rating for the book
            Double avg = em.createQuery(
                            "SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bid",
                            Double.class)
                    .setParameter("bid", bookId)
                    .getSingleResult();
            bookRef.setAverageRating(avg != null ? avg : 0.0);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
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
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                System.out.println("User đã like review này rồi!");
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

            Review review = em.find(Review.class, reviewId);
            if (review == null) {
                tx.rollback();
                System.out.println("Review không tồn tại");
                return false;
            }

            TypedQuery<model.LikeReview> query = em.createQuery(
                    "SELECT lr FROM LikeReview lr WHERE lr.review.id = :reviewId AND lr.user.id = :userId",
                    model.LikeReview.class
            );
            query.setParameter("reviewId", reviewId);
            query.setParameter("userId", userId);

            List<model.LikeReview> results = query.getResultList();

            if (results.isEmpty()) {
                tx.rollback();
                System.out.println("User chưa like review này, không thể dislike.");
                return false;
            }

            model.LikeReview likeReview = results.get(0);
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
