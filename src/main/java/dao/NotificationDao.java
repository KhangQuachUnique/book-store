package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Notification;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationDao {
    private static final Logger log = Logger.getLogger(NotificationDao.class.getName());

    // Đếm số thông báo CHƯA ĐỌC của một user
    public static int countUnreadByUserId(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = FALSE", Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult().intValue();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi đếm thông báo chưa đọc cho user " + userId, e);
            return 0;
        } finally {
            em.close();
        }
    }

    // Lấy tất cả thông báo của một user, sắp xếp mới nhất lên đầu
    public static List<Notification> findByUserId(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Notification> query = em.createQuery(
                "SELECT n FROM Notification n LEFT JOIN FETCH n.user WHERE n.user.id = :userId ORDER BY n.createdAt DESC", Notification.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy danh sách thông báo cho user " + userId, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    // Đánh dấu tất cả thông báo của user là đã đọc
    public static void markAllAsRead(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            int updated = em.createQuery(
                "UPDATE Notification n SET n.isRead = TRUE WHERE n.user.id = :userId AND n.isRead = FALSE")
                .setParameter("userId", userId)
                .executeUpdate();
            em.getTransaction().commit();
            log.info("Đã đánh dấu " + updated + " thông báo là đã đọc cho user " + userId);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Lỗi khi đánh dấu thông báo đã đọc cho user " + userId, e);
        } finally {
            em.close();
        }
    }
}
