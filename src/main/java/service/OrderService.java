package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import util.JPAUtil;

import java.util.List;

public class OrderService {

    /**
     * Lấy danh sách đơn theo user + trạng thái
     */
    public List<Order> getOrdersByUserAndStatus(Long userId, String statusParam) {
        EntityManager em = JPAUtil.getEntityManager();
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

            // ✅ Tính tổng cho từng order (subtotal, discount, final)
            for (Order o : orders) {
                o.calculateTotals();
            }

            return orders;
        } finally {
            em.close();
        }
    }

    /**
     * 🧾 Tạo đơn hàng mới (tính giá và lưu)
     */
    public void createOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
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
}
