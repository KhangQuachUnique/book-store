package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderStatus;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger log = Logger.getLogger(OrderDAO.class.getName());

    public static List<Order> getOrdersByUserIdAndStatus(Long userId, String statusFilter) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.user WHERE o.user.id = :userId");
            
            if (statusFilter != null && !"all".equals(statusFilter)) {
                try {
                    OrderStatus.valueOf(statusFilter.toUpperCase()); // Validate status exists
                    jpql.append(" AND o.status = :status");
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore filter
                }
            }
            
            jpql.append(" ORDER BY o.createdAt DESC");
            
            TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class);
            query.setParameter("userId", userId);
            
            if (statusFilter != null && !"all".equals(statusFilter)) {
                try {
                    OrderStatus status = OrderStatus.valueOf(statusFilter.toUpperCase());
                    query.setParameter("status", status);
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore filter
                }
            }
            
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy orders cho user " + userId + " với status " + statusFilter, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public static Order getOrderById(Long orderId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery(
                "SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.user WHERE o.id = :orderId", Order.class);
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Lỗi khi lấy order với ID: " + orderId, e);
            return null;
        } finally {
            em.close();
        }
    }
}
