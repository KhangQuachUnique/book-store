package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderStatus;
import util.JPAUtil;

import java.util.List;

public class OrderService {

    /**
     * Lấy đơn theo user + trạng thái.
     * statusParam = null hoặc "ALL" -> lấy tất cả.
     * Dùng JOIN FETCH để nạp sẵn items & book, tránh LazyInitializationException khi render JSP.
     */
    public List<Order> getOrdersByUserAndStatus(Long userId, String statusParam) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            boolean all = (statusParam == null || "ALL".equalsIgnoreCase(statusParam));
            StringBuilder jpql = new StringBuilder(
                    "SELECT DISTINCT o FROM Order o " +
                            "LEFT JOIN FETCH o.items oi " +
                            "LEFT JOIN FETCH oi.book b " +  // JSP đang dùng item.book.title/thumbnail/price
                            "WHERE o.user.id = :userId "
            );

            if (!all) {
                jpql.append("AND o.status = :status ");
            }

            jpql.append("ORDER BY o.createdAt DESC");

            TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class)
                    .setParameter("userId", userId);

            if (!all) {
                // Chuẩn hoá về UPPER để tránh lỗi valueOf khi client truyền thường
                OrderStatus status = OrderStatus.valueOf(statusParam.toUpperCase());
                query.setParameter("status", status);
            }

            return query.getResultList();
        } finally {
            em.close(); // Trả về entity đã fetch sẵn associations cần dùng
        }
    }
}
