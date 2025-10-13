package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Notification;
import model.User;
import util.JPAUtil;

import java.util.List;

/**
 * Data Access Object cho Notification
 * Xử lý các thao tác truy xuất dữ liệu liên quan đến Notification
 */
public class NotificationDao {

    /**
     * Đếm số thông báo chưa đọc của một người dùng
     */
    public int countUnreadByUserId(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(n) FROM Notification n WHERE n.user.id = :uid AND n.isRead = false",
                            Long.class)
                    .setParameter("uid", userId)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    /**
     * Tìm tất cả thông báo của một người dùng, sắp xếp theo ID giảm dần
     */
    public List<Notification> findByUserId(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Notification> q = em.createQuery(
                    "SELECT n FROM Notification n WHERE n.user.id = :uid ORDER BY n.createdAt DESC",
                    Notification.class);
            q.setParameter("uid", userId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Đánh dấu tất cả thông báo của một người dùng là đã đọc
     */
    public void markAllAsRead(long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :uid AND (n.isRead = false OR n.isRead IS NULL)")
                    .setParameter("uid", userId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Tìm thông báo theo ID
     */
    // public Notification findById(Long id) {
    //     EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
    //     try {
    //         return em.find(Notification.class, id);
    //     } finally {
    //         em.close();
    //     }
    // }

    /**
     * Lưu hoặc cập nhật một thông báo
     */
    // public Notification save(Notification n) {
    //     EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
    //     EntityTransaction tx = em.getTransaction();
    //     try {
    //         tx.begin();
    //         Notification result;
    //         if (n.getId() == null) {
    //             em.persist(n);
    //             result = n;
    //         } else {
    //             result = em.merge(n);
    //         }
    //         tx.commit();
    //         return result;
    //     } catch (Exception ex) {
    //         if (tx.isActive()) tx.rollback();
    //         throw ex;
    //     } finally {
    //         em.close();
    //     }
    // }

    /**
     * Tạo thông báo mới cho một người dùng
     */
    // public Notification createForUser(long userId, String title, String message) {
    //     EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
    //     EntityTransaction tx = em.getTransaction();
    //     try {
    //         tx.begin();
    //         User user = em.getReference(User.class, userId);
            
    //         Notification notification = new Notification();
    //         notification.setTitle(title);
    //         notification.setMessage(message);
    //         notification.setIsRead(false);
    //         notification.setUser(user);
            
    //         em.persist(notification);
    //         tx.commit();
    //         return notification;
    //     } catch (Exception ex) {
    //         if (tx.isActive()) tx.rollback();
    //         throw ex;
    //     } finally {
    //         em.close();
    //     }
    // }

    /**
     * Xóa một thông báo theo ID
     */
    // public void delete(Long id) {
    //     EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
    //     EntityTransaction tx = em.getTransaction();
    //     try {
    //         tx.begin();
    //         Notification found = em.find(Notification.class, id);
    //         if (found != null) {
    //             em.remove(found);
    //         }
    //         tx.commit();
    //     } catch (Exception ex) {
    //         if (tx.isActive()) tx.rollback();
    //         throw ex;
    //     } finally {
    //         em.close();
    //     }
    // }
}
