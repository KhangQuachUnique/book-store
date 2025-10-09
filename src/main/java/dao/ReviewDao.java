package dao;

import jakarta.persistence.*;
import model.Review;
import util.JPAUtil;

import java.util.List;

public class ReviewDao {
    private final EntityManagerFactory emf;

    public ReviewDao() {
        // Lấy EMF duy nhất từ JpaUtil
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    /**
     * Lấy danh sách review kèm số lượt like và trạng thái liked của currentUser
     */
    public List<Review> getReviewsByBookId(Long bookId, Long currentUserId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = """
                SELECT r,
                       COUNT(l.id) AS likeCount,
                       CASE WHEN SUM(CASE WHEN l.user.id = :currentUserId THEN 1 ELSE 0 END) > 0 
                            THEN TRUE ELSE FALSE END AS likedByCurrentUser
                FROM Review r
                LEFT JOIN r.likes l
                WHERE r.book.id = :bookId
                GROUP BY r.id
                ORDER BY r.createdAt DESC
            """;

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("bookId", bookId);
            query.setParameter("currentUserId", currentUserId);

            List<Object[]> results = query.getResultList();

            // Map Object[] sang Review + transient fields
            for (Object[] row : results) {
                Review r = (Review) row[0];
                Long likeCount = (Long) row[1];
                Boolean liked = (Boolean) row[2];
                r.setLikeCount(likeCount);
                r.setLikedByCurrentUser(liked);
            }

            // Chỉ trả về List<Review>
            return results.stream()
                    .map(row -> (Review) row[0])
                    .toList();

        } finally {
            em.close();
        }
    }
}
