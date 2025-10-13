package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.Category;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryBookDao {
    private static final int BOOKS_PER_PAGE = 40;

    /**
     * Lấy tất cả sách với phân trang
     */
    public static List<Book> getAllBook(int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT b FROM Book b ORDER BY b.id";
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            query.setFirstResult((page - 1) * BOOKS_PER_PAGE);
            query.setMaxResults(BOOKS_PER_PAGE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Đếm tổng số sách
     */
    public static long getTotalBooks() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Book b";
            return em.createQuery(jpql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy sách theo category ID với phân trang
     */
    public static List<Book> getBooksByCategoryId(int categoryId, int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT b FROM Book b WHERE b.category.id = :categoryId ORDER BY b.id";
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            query.setParameter("categoryId", (long) categoryId);
            query.setFirstResult((page - 1) * BOOKS_PER_PAGE);
            query.setMaxResults(BOOKS_PER_PAGE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Đếm tổng số sách theo category ID
     */
    public static long getTotalBooksByCategory(int categoryId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("categoryId", (long) categoryId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Lọc sách theo tiêu đề, tác giả, và categories (include/exclude)
     */
    public static List<Book> filterBooks(String title, String author,
                                         List<Long> includeCategories,
                                         List<Long> excludeCategories,
                                         int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");

            // Tìm kiếm theo title hoặc author với OR logic
            if ((title != null && !title.trim().isEmpty()) || (author != null && !author.trim().isEmpty())) {
                jpql.append(" AND (");
                if (title != null && !title.trim().isEmpty()) {
                    jpql.append("LOWER(b.title) LIKE LOWER(:searchTerm)");
                }
                if ((title != null && !title.trim().isEmpty()) && (author != null && !author.trim().isEmpty())) {
                    jpql.append(" OR ");
                }
                if (author != null && !author.trim().isEmpty()) {
                    jpql.append("LOWER(b.author) LIKE LOWER(:searchTerm)");
                }
                jpql.append(")");
            }

            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }

            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }

            jpql.append(" ORDER BY b.id");

            TypedQuery<Book> query = em.createQuery(jpql.toString(), Book.class);

            // Sử dụng cùng một tham số searchTerm cho cả title và author
            if ((title != null && !title.trim().isEmpty()) || (author != null && !author.trim().isEmpty())) {
                String searchTerm = title != null && !title.trim().isEmpty() ? title.trim() : author.trim();
                query.setParameter("searchTerm", "%" + searchTerm + "%");
            }

            if (includeCategories != null && !includeCategories.isEmpty()) {
                query.setParameter("includeCategories", includeCategories);
            }

            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                query.setParameter("excludeCategories", excludeCategories);
            }

            query.setFirstResult((page - 1) * BOOKS_PER_PAGE);
            query.setMaxResults(BOOKS_PER_PAGE);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Đếm số sách theo filter
     */
    public static long countBooks(String title, String author,
                                  List<Long> includeCategories,
                                  List<Long> excludeCategories) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");

            // Tìm kiếm theo title hoặc author với OR logic
            if ((title != null && !title.trim().isEmpty()) || (author != null && !author.trim().isEmpty())) {
                jpql.append(" AND (");
                if (title != null && !title.trim().isEmpty()) {
                    jpql.append("LOWER(b.title) LIKE LOWER(:searchTerm)");
                }
                if ((title != null && !title.trim().isEmpty()) && (author != null && !author.trim().isEmpty())) {
                    jpql.append(" OR ");
                }
                if (author != null && !author.trim().isEmpty()) {
                    jpql.append("LOWER(b.author) LIKE LOWER(:searchTerm)");
                }
                jpql.append(")");
            }

            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }

            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

            // Sử dụng cùng một tham số searchTerm cho cả title và author
            if ((title != null && !title.trim().isEmpty()) || (author != null && !author.trim().isEmpty())) {
                String searchTerm = title != null && !title.trim().isEmpty() ? title.trim() : author.trim();
                query.setParameter("searchTerm", "%" + searchTerm + "%");
            }

            if (includeCategories != null && !includeCategories.isEmpty()) {
                query.setParameter("includeCategories", includeCategories);
            }

            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                query.setParameter("excludeCategories", excludeCategories);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy tất cả categories
     */
    public static List<Category> getAllCategories() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT c FROM Category c ORDER BY c.name";
            return em.createQuery(jpql, Category.class).getResultList();
        } finally {
            em.close();
        }
    }
}

