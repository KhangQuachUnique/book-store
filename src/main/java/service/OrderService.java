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
                            "LEFT JOIN FETCH o.promotion p " + // ✅ thêm để lấy luôn promotion
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

            // ✅ Tính lại tổng tiền sau khuyến mãi (chỉ để hiển thị)
            for (Order o : orders) {
                double subtotal = 0;
                if (o.getItems() != null) {
                    subtotal = o.getItems().stream()
                            .mapToDouble(i -> i.getPrice() * i.getQuantity())
                            .sum();
                }

                double discountAmount = 0;
                if (o.getPromotion() != null) {
                    discountAmount = subtotal * o.getPromotion().getDiscount() / 100.0;
                }

                o.setTotalAmount(subtotal - discountAmount);
            }

            return orders;
        } finally {
            em.close();
        }
    }


    /**
     * 🧾 Hàm tạo đơn hàng mới — lưu cả giá từng item và tổng tiền
     */
    public void createOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            double totalAmount = 0.0;

            for (OrderItem item : order.getItems()) {
                // ✅ Tính giá thật tại thời điểm checkout
                double discountedPrice = item.getBook().getOriginalPrice()
                        * (1 - (item.getBook().getDiscountRate() / 100.0));

                item.setPrice(discountedPrice); // Lưu giá tại thời điểm mua
                item.setOrder(order);           // Gán quan hệ ngược
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
