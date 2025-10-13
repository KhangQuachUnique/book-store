package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.User;
import model.ViewedProduct;
import model.ViewedProductItem;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewHistoryDao {
    private static final Logger log = Logger.getLogger(ViewHistoryDao.class.getName());

    public void createViewHistoryForUser(Long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.getReference(User.class, userId);
            ViewedProduct viewedProduct = new ViewedProduct();
            viewedProduct.setUser(user);
            em.persist(viewedProduct);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Lỗi khi tạo lịch sử xem cho user " + userId, e);
        } finally {
            em.close();
        }
    }

    /**
     * Lấy lịch sử xem sách theo userId
     */
    public static List<ViewedProductItem> getHistoryByUserId(Long userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<ViewedProductItem> query = em.createQuery(
                "SELECT vpi FROM ViewedProductItem vpi " +
                "LEFT JOIN FETCH vpi.book " +
                "LEFT JOIN FETCH vpi.book.category " +
                "WHERE vpi.viewedProduct.user.id = :userId " +
                "ORDER BY vpi.id DESC", ViewedProductItem.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy lịch sử xem cho user " + userId, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Ghi lại lịch sử xem sách
     */
    public static void addHistory(Long userId, Long bookId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            log.fine("[ViewHistory] Begin addHistory userId=" + userId + ", bookId=" + bookId);

            // Find or create ViewedProduct cho user
            ViewedProduct viewedProduct;
            try {
                TypedQuery<ViewedProduct> query = em.createQuery(
                    "SELECT vp FROM ViewedProduct vp WHERE vp.user.id = :userId", ViewedProduct.class);
                query.setParameter("userId", userId);
                viewedProduct = query.getSingleResult();
                log.fine("[ViewHistory] Found existing ViewedProduct id=" + viewedProduct.getId());
            } catch (NoResultException e) {
                // Tạo mới ViewedProduct cho user
                viewedProduct = new ViewedProduct();
                User user = em.getReference(User.class, userId);
                viewedProduct.setUser(user);
                em.persist(viewedProduct);
                em.flush(); // đảm bảo ID được tạo
                log.fine("[ViewHistory] Created new ViewedProduct id=" + viewedProduct.getId());
            }
            
            // Tạo ViewedProductItem mới
            ViewedProductItem item = new ViewedProductItem();
            Book book = em.getReference(Book.class, bookId);
            item.setBook(book);
            item.setViewedProduct(viewedProduct);
            // set timestamp để tránh lỗi NOT NULL
            item.setViewedAt(new Timestamp(System.currentTimeMillis()));
            em.persist(item);
            em.flush(); // force insert để phát hiện lỗi DB sớm

            em.getTransaction().commit();
            log.fine("[ViewHistory] Saved ViewedProductItem id=" + item.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Lỗi khi ghi lại lịch sử xem cho user " + userId + ", book " + bookId, e);
        } finally {
            em.close();
        }
    }
}
