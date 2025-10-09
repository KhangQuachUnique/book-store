package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderStatus;
import util.JPAUtil;

import java.util.List;

public class OrderDAO {

    /**
     * Lấy danh sách đơn hàng theo userId và (tùy chọn) trạng thái.
     * Nếu statusParam = "ALL" hoặc null → trả về tất cả đơn.
     */
    public List<Order> getOrdersByUserIdAndStatus(Long userId, String statusParam) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.items " +
                    "WHERE o.user.id = :userId";

            boolean all = (statusParam == null || "ALL".equalsIgnoreCase(statusParam));

            if (!all) {
                jpql += " AND o.status = :status";
            }

            jpql += " ORDER BY o.createdAt DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("userId", userId);

            if (!all) {
                query.setParameter("status", OrderStatus.valueOf(statusParam));
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy chi tiết một đơn hàng theo ID.
     */
    public Order getOrderById(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.items " +
                    "LEFT JOIN FETCH o.user " +
                    "LEFT JOIN FETCH o.promotion " +
                    "WHERE o.id = :orderId";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderId", orderId);

            List<Order> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng.
     */
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                order.setStatus(newStatus);
                em.merge(order);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Tạo mới một đơn hàng.
     */
    public void saveOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Xóa đơn hàng (nếu cần thiết).
     */
    public void deleteOrder(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                em.remove(order);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
