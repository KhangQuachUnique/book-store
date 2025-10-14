package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import util.JPAUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDAO {

    private final EntityManagerFactory emf;

    public OrderDAO() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    /**
     * Lấy danh sách đơn hàng theo userId và (tùy chọn) trạng thái.
     * Nếu statusParam = "ALL" hoặc null → trả về tất cả đơn.
     */
    public List<Order> getOrdersByUserIdAndStatus(Long userId, String statusParam) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean all = (statusParam == null || "ALL".equalsIgnoreCase(statusParam));
            StringBuilder jpql = new StringBuilder(
                    "SELECT DISTINCT o FROM Order o " +
                            "LEFT JOIN FETCH o.items oi " +
                            "LEFT JOIN FETCH oi.book b " +
                            "LEFT JOIN FETCH o.promotion p " +
                            "WHERE o.user.id = :userId "
            );

            if (!all) {
                jpql.append("AND o.status = :status ");
            }

            jpql.append("ORDER BY o.createdAt DESC");

            TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class)
                    .setParameter("userId", userId);

            if (!all) {
                OrderStatus status = OrderStatus.valueOf(statusParam.toUpperCase());
                query.setParameter("status", status);
            }

            List<Order> orders = query.getResultList();

            //Tính tổng cho từng order (subtotal, discount, final)
            for (Order o : orders) {
                o.calculateTotals();
            }

            return orders;
        } finally {
            em.close();
        }
    }

    /**
     * Lấy chi tiết một đơn hàng theo ID.
     */
    public Order getOrderById(long orderId) throws SQLException {
        EntityManager em = emf.createEntityManager();
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
        EntityManager em = emf.createEntityManager();
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
    public void createOrder(Order order) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            double totalAmount = 0.0;

            for (OrderItem item : order.getItems()) {
                double discountedPrice = item.getBook().getOriginalPrice()
                        * (1 - (item.getBook().getDiscountRate() / 100.0));
                item.setPrice(discountedPrice);
                item.setOrder(order);
                totalAmount += discountedPrice * item.getQuantity();
            }

            order.setTotalAmount(totalAmount);
            em.persist(order);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Xóa đơn hàng (nếu cần thiết).
     */
    public void deleteOrder(Long orderId) {
        EntityManager em = emf.createEntityManager();
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

    /**
     * Check if user purchased (delivered) a specific book.
     */
    public boolean userPurchasedBookDelivered(Long userId, Long bookId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(oi) FROM OrderItem oi " +
                                    "WHERE oi.order.user.id = :uid AND oi.book.id = :bid " +
                                    "AND oi.order.status = :st",
                            Long.class)
                    .setParameter("uid", userId)
                    .setParameter("bid", bookId)
                    .setParameter("st", OrderStatus.DELIVERED)
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}