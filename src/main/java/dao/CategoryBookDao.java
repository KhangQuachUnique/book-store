package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.Category;
import util.JPAUtil;

import java.util.List;

public class CategoryBookDao {
    private static final int BOOKS_PER_PAGE = 40;
    private static final String DISCOUNTED_PRICE_JPQL = 
            "b.originalPrice * (1 - COALESCE(b.discountRate, 0) / 100.0)";
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
     * Lọc sách theo tiêu đề, năm xuất bản, và categories (include/exclude)
     */
    public static List<Book> filterBooks(String title, Integer publishYear, 
                                         List<Long> includeCategories, 
                                         List<Long> excludeCategories, 
                                         int page,
                                         String sortBy,
                                         String author,
                                         Integer yearBefore, 
                                         Integer yearAfter,  
                                         Long priceFrom,     
                                         Long priceUpTo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");
            
            if (title != null && !title.trim().isEmpty()) {
                jpql.append(" AND LOWER(b.title) LIKE LOWER(:title)");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                jpql.append(" AND LOWER(b.author) LIKE LOWER(:author)");
            }

            if (publishYear != null) {
                jpql.append(" AND b.publishYear = :publishYear");
            }
            
            if (yearBefore != null) {
                jpql.append(" AND b.publishYear <= :yearBefore");
            }
            
            if (yearAfter != null) {
                jpql.append(" AND b.publishYear >= :yearAfter");
            }

            if (priceFrom != null) {
                jpql.append(" AND ").append(DISCOUNTED_PRICE_JPQL).append(" >= :priceFrom"); // ✅ Dùng công thức
            }
        
            if (priceUpTo != null) {
                jpql.append(" AND ").append(DISCOUNTED_PRICE_JPQL).append(" <= :priceUpTo"); // ✅ Dùng công thức
            }         

            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }
            
            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }
            
            jpql.append(getOrderClause(sortBy));
            
            TypedQuery<Book> query = em.createQuery(jpql.toString(), Book.class);
            
            if (title != null && !title.trim().isEmpty()) {
                query.setParameter("title", "%" + title.trim() + "%");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                query.setParameter("author", "%" + author.trim() + "%");
            }
            
            if (yearBefore != null) {
                query.setParameter("yearBefore", yearBefore);
            }
            
            if (yearAfter != null) {
                query.setParameter("yearAfter", yearAfter); 
            }
            
            if (priceFrom != null) {
                query.setParameter("priceFrom", priceFrom.doubleValue());
            }
        
            if (priceUpTo != null) {
                query.setParameter("priceUpTo", priceUpTo.doubleValue());
            }
            
            if (publishYear != null) {
                query.setParameter("publishYear", publishYear);
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
    public static long countBooks(String title, Integer publishYear, 
                                  List<Long> includeCategories, 
                                  List<Long> excludeCategories,
                                  String author,
                                  Integer yearBefore, 
                                  Integer yearAfter,  
                                  Long priceFrom,     
                                  Long priceUpTo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");
            
            if (title != null && !title.trim().isEmpty()) {
                jpql.append(" AND LOWER(b.title) LIKE LOWER(:title)");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                jpql.append(" AND LOWER(b.author) LIKE LOWER(:author)");
            }

            if (publishYear != null) {
                jpql.append(" AND b.publishYear = :publishYear");
            }
            
            if (yearBefore != null) {
                jpql.append(" AND b.publishYear <= :yearBefore");
            }            
            
            if (yearAfter != null) {
                jpql.append(" AND b.publishYear >= :yearAfter");
            }

            if (priceFrom != null) {
                jpql.append(" AND ").append(DISCOUNTED_PRICE_JPQL).append(" >= :priceFrom"); 
            
            }

            if (priceUpTo != null) {
                jpql.append(" AND ").append(DISCOUNTED_PRICE_JPQL).append(" <= :priceUpTo"); 
            }

            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }
            
            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }
            
            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            
            if (title != null && !title.trim().isEmpty()) {
                query.setParameter("title", "%" + title.trim() + "%");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                query.setParameter("author", "%" + author.trim() + "%");
            }

            if (publishYear != null) {
                query.setParameter("publishYear", publishYear);
            }
            
            if (yearBefore != null) {
                query.setParameter("yearBefore", yearBefore);
            }
            
            if (yearAfter != null) {
                query.setParameter("yearAfter", yearAfter); 
            }
            
            if (priceFrom != null) {
                query.setParameter("priceFrom", priceFrom.doubleValue()); 
            }
            
            if (priceUpTo != null) {
                query.setParameter("priceUpTo", priceUpTo.doubleValue());
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

    // sort toàn bộ sách
    public static List<Book> sortAllBooks(String sortBy, int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT b FROM Book b" + getOrderClause(sortBy);
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            query.setFirstResult((page - 1) * BOOKS_PER_PAGE);
            query.setMaxResults(BOOKS_PER_PAGE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //  sort theo từng category
    public static List<Book> sortBooksByCategory(int categoryId, String sortBy, int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT b FROM Book b WHERE b.category.id = :categoryId" + getOrderClause(sortBy);
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            query.setParameter("categoryId", (long) categoryId);
            query.setFirstResult((page - 1) * BOOKS_PER_PAGE);
            query.setMaxResults(BOOKS_PER_PAGE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //  helper sinh ORDER BY hợp lệ
    private static String getOrderClause(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) return " ORDER BY b.id";
        switch (sortBy) {
            case "title_asc":   return " ORDER BY LOWER(b.title) ASC";
            case "title_desc":  return " ORDER BY LOWER(b.title) DESC";
            case "rating_high": return " ORDER BY b.averageRating DESC";
            case "rating_low":  return " ORDER BY b.averageRating ASC";
            case "year_asc":    return " ORDER BY b.publishYear ASC";
            case "year_desc":   return " ORDER BY b.publishYear DESC";
            case "price_high":  return " ORDER BY " + DISCOUNTED_PRICE_JPQL + " DESC";
            case "price_low":   return " ORDER BY " + DISCOUNTED_PRICE_JPQL + " ASC";
            default:            return " ORDER BY b.id";
        }
    }
}
