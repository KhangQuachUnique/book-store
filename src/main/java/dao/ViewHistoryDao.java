package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.User;
import model.ViewedProduct;
import model.ViewedProductItem;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewHistoryDao {
    private static final Logger log = Logger.getLogger(ViewHistoryDao.class.getName());

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
            
            // Find or create ViewedProduct for user
            ViewedProduct viewedProduct = null;
            try {
                TypedQuery<ViewedProduct> query = em.createQuery(
                    "SELECT vp FROM ViewedProduct vp WHERE vp.user.id = :userId", ViewedProduct.class);
                query.setParameter("userId", userId);
                viewedProduct = query.getSingleResult();
            } catch (NoResultException e) {
                // Create new ViewedProduct for user
                viewedProduct = new ViewedProduct();
                User user = em.getReference(User.class, userId);
                viewedProduct.setUser(user);
                em.persist(viewedProduct);
                em.flush(); // Ensure ID is generated
            }
            
            // Create new ViewedProductItem
            ViewedProductItem item = new ViewedProductItem();
            Book book = em.getReference(Book.class, bookId.intValue());
            item.setBook(book);
            item.setViewedProduct(viewedProduct);
            em.persist(item);
            
            em.getTransaction().commit();
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
