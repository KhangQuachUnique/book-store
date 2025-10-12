package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import model.Order;
import model.OrderStatus;
import util.JPAUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrderManagementDAO {
    private final EntityManagerFactory emf;

    public OrderManagementDAO() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    /**
     * Lấy tất cả đơn hàng (cho admin)
     */
    public List<Order> getAllOrders() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.user u " +
                    "LEFT JOIN FETCH o.promotion p " +
                    "LEFT JOIN FETCH o.items oi " +
                    "LEFT JOIN FETCH oi.book b " +
                    "ORDER BY o.createdAt DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            List<Order> orders = query.getResultList();

            // Calculate totals for each order
            for (Order order : orders) {
                order.calculateTotals();
            }

            return orders;
        } catch (Exception e) {
            throw new SQLException("Error fetching all orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Tìm kiếm đơn hàng theo từ khóa (ID, username, email)
     */
    public List<Order> searchOrders(String keyword) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.user u " +
                    "LEFT JOIN FETCH o.promotion p " +
                    "LEFT JOIN FETCH o.items oi " +
                    "LEFT JOIN FETCH oi.book b " +
                    "WHERE CAST(o.id AS string) LIKE :keyword " +
                    "OR LOWER(u.name) LIKE LOWER(:keyword) " +
                    "OR LOWER(u.email) LIKE LOWER(:keyword) " +
                    "ORDER BY o.createdAt DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("keyword", "%" + keyword + "%");

            List<Order> orders = query.getResultList();

            for (Order order : orders) {
                order.calculateTotals();
            }

            return orders;
        } catch (Exception e) {
            throw new SQLException("Error searching orders", e);
        } finally {
            em.close();
        }
    }

    /**
     * Lọc đơn hàng theo trạng thái
     */
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.user u " +
                    "LEFT JOIN FETCH o.promotion p " +
                    "LEFT JOIN FETCH o.items oi " +
                    "LEFT JOIN FETCH oi.book b " +
                    "WHERE o.status = :status " +
                    "ORDER BY o.createdAt DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("status", OrderStatus.valueOf(status.toUpperCase()));

            List<Order> orders = query.getResultList();

            for (Order order : orders) {
                order.calculateTotals();
            }

            return orders;
        } catch (Exception e) {
            throw new SQLException("Error fetching orders by status", e);
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean updateOrderStatus(long orderId, String status) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Order order = em.find(Order.class, orderId);
            if (order == null) {
                return false;
            }

            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            em.merge(order);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error updating order status", e);
        } finally {
            em.close();
        }
    }

    /**
     * Xóa đơn hàng
     */
    public boolean deleteOrder(long orderId) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Order order = em.find(Order.class, orderId);
            if (order == null) {
                return false;
            }

            em.remove(order);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error deleting order", e);
        } finally {
            em.close();
        }
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    public Order getOrderById(long orderId) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.user u " +
                    "LEFT JOIN FETCH o.promotion p " +
                    "LEFT JOIN FETCH o.items oi " +
                    "LEFT JOIN FETCH oi.book b " +
                    "WHERE o.id = :orderId";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderId", orderId);

            List<Order> result = query.getResultList();
            if (result.isEmpty()) {
                return null;
            }

            Order order = result.get(0);
            order.calculateTotals();
            return order;
        } catch (Exception e) {
            throw new SQLException("Error fetching order details", e);
        } finally {
            em.close();
        }
    }

    /**
     * Thống kê doanh thu theo ngày
     */
    public double getDailyRevenue() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
                    "WHERE CAST(o.createdAt AS date) = CURRENT_DATE " +
                    "AND o.status != model.OrderStatus.CANCELLED";

            return em.createQuery(jpql, Double.class).getSingleResult();
        } catch (Exception e) {
            throw new SQLException("Error fetching daily revenue", e);
        } finally {
            em.close();
        }
    }

    /**
     * Số đơn hàng trong ngày
     */
    public long getDailyOrderCount() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(o) FROM Order o " +
                    "WHERE CAST(o.createdAt AS date) = CURRENT_DATE";

            return em.createQuery(jpql, Long.class).getSingleResult();
        } catch (Exception e) {
            throw new SQLException("Error fetching daily order count", e);
        } finally {
            em.close();
        }
    }

    /**
     * Thống kê doanh thu theo tháng
     */
    public double getMonthlyRevenue() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o " +
                    "WHERE YEAR(o.createdAt) = YEAR(CURRENT_DATE) " +
                    "AND MONTH(o.createdAt) = MONTH(CURRENT_DATE) " +
                    "AND o.status != model.OrderStatus.CANCELLED";

            return em.createQuery(jpql, Double.class).getSingleResult();
        } catch (Exception e) {
            throw new SQLException("Error fetching monthly revenue", e);
        } finally {
            em.close();
        }
    }

    /**
     * Số đơn hàng trong tháng
     */
    public long getMonthlyOrderCount() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(o) FROM Order o " +
                    "WHERE YEAR(o.createdAt) = YEAR(CURRENT_DATE) " +
                    "AND MONTH(o.createdAt) = MONTH(CURRENT_DATE)";

            return em.createQuery(jpql, Long.class).getSingleResult();
        } catch (Exception e) {
            throw new SQLException("Error fetching monthly order count", e);
        } finally {
            em.close();
        }
    }

    /**
     * Top sách bán chạy
     */
    public List<Map<String, Object>> getTopSellingBooks(int limit) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT b.title as title, SUM(oi.quantity) as totalSold " +
                    "FROM OrderItem oi " +
                    "JOIN oi.book b " +
                    "JOIN oi.order o " +
                    "WHERE o.status != model.OrderStatus.CANCELLED " +
                    "GROUP BY b.id, b.title " +
                    "ORDER BY totalSold DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setMaxResults(limit);

            List<Object[]> results = query.getResultList();
            List<Map<String, Object>> books = new java.util.ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> book = new HashMap<>();
                book.put("title", row[0]);
                book.put("totalSold", row[1]);
                books.add(book);
            }

            return books;
        } catch (Exception e) {
            throw new SQLException("Error fetching top selling books", e);
        } finally {
            em.close();
        }
    }
}
