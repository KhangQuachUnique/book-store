package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.Category;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookDao {
    private static final Logger LOGGER = Logger.getLogger(BookDao.class.getName());
    private static final int PAGE_SIZE = 20;

    public List<Book> getAllBooks(int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b JOIN FETCH b.category ORDER BY b.id", Book.class);
            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
            List<Book> result = query.getResultList();
            LOGGER.info("Fetched " + result.size() + " books for page " + page);
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    public Book getBookById(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Book book = em.find(Book.class, (int) id);
            if (book != null) {
                LOGGER.info("Fetched book with ID " + id + ": " + book.getTitle());
            }
            return book;
        } finally {
            em.close();
        }
    }

    public boolean addBook(Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            book.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            em.persist(book);
            em.getTransaction().commit();
            LOGGER.info("Added book: " + book.getTitle());
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error adding book: " + e.getMessage());
            throw new RuntimeException("Error adding book", e);
        } finally {
            em.close();
        }
    }

    public boolean updateBook(Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
            LOGGER.info("Updated book: " + book.getTitle());
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error updating book: " + e.getMessage());
            throw new RuntimeException("Error updating book", e);
        } finally {
            em.close();
        }
    }

    public boolean deleteBook(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, (int) id);
            if (book != null) {
                em.remove(book);
                em.getTransaction().commit();
                LOGGER.info("Deleted book with ID: " + id);
                return true;
            }
            LOGGER.warning("Book not found for deletion: ID " + id);
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error deleting book: " + e.getMessage());
            throw new RuntimeException("Error deleting book", e);
        } finally {
            em.close();
        }
    }

    public List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
                                  List<Long> excludeCategories, int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT b FROM Book b JOIN FETCH b.category WHERE 1=1");
            if (title != null && !title.isEmpty()) {
                jpql.append(" AND LOWER(b.title) LIKE :title");
            }
            if (publishYear != null) {
                jpql.append(" AND b.publishYear = :publishYear");
            }
            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }
            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }
            jpql.append(" ORDER BY b.id");

            TypedQuery<Book> query = em.createQuery(jpql.toString(), Book.class);
            if (title != null && !title.isEmpty()) {
                query.setParameter("title", "%" + title.toLowerCase() + "%");
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
            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
            List<Book> result = query.getResultList();
            LOGGER.info("Fetched " + result.size() + " books for filter: title=" + title + ", page=" + page);
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    public long countBooks(String title, Integer publishYear, List<Long> includeCategories,
                           List<Long> excludeCategories) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");
            if (title != null && !title.isEmpty()) {
                jpql.append(" AND LOWER(b.title) LIKE :title");
            }
            if (publishYear != null) {
                jpql.append(" AND b.publishYear = :publishYear");
            }
            if (includeCategories != null && !includeCategories.isEmpty()) {
                jpql.append(" AND b.category.id IN :includeCategories");
            }
            if (excludeCategories != null && !excludeCategories.isEmpty()) {
                jpql.append(" AND b.category.id NOT IN :excludeCategories");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            if (title != null && !title.isEmpty()) {
                query.setParameter("title", "%" + title.toLowerCase() + "%");
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
            long count = query.getSingleResult();
            LOGGER.info("Counted " + count + " books for filter: title=" + title);
            return count;
        } finally {
            em.close();
        }
    }

    public List<Category> getAllCategories() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class);
            List<Category> result = query.getResultList();
            LOGGER.info("Fetched " + result.size() + " categories");
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    public Category getCategoryById(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, id);
            LOGGER.info("Fetched category with ID " + id);
            return category;
        } finally {
            em.close();
        }
    }

    public boolean logImport(String tableName, String importedData) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("INSERT INTO import_logs (table_name, imported_data, imported_at) VALUES (?1, ?2, CURRENT_TIMESTAMP)")
                    .setParameter(1, tableName)
                    .setParameter(2, importedData)
                    .executeUpdate();
            em.getTransaction().commit();
            LOGGER.info("Logged import for table: " + tableName);
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error logging import: " + e.getMessage());
            throw new RuntimeException("Error logging import", e);
        } finally {
            em.close();
        }
    }
}