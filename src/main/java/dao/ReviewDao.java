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
}
