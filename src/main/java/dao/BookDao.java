package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.Category;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for managing books in the database using JPA.
 */
public class BookDao {
    private static final Logger log = Logger.getLogger(BookDao.class.getName());
    private static final int PAGE_SIZE = 20; // Limit to 20 books per page

    /**
     * Retrieves all books with pagination.
     *
     * @param page The page number (1-based).
     * @return List of books for the specified page.
     */
    public List<Book> getAllBooks(int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b JOIN FETCH b.category ORDER BY b.id", Book.class);
            query.setFirstResult((page - 1) * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
            List<Book> result = query.getResultList();
            log.info("Fetched " + result.size() + " books for page " + page);
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The book ID.
     * @return The Book object or null if not found.
     */
    public Book getBookById(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Book book = em.find(Book.class, (int) id);
            if (book != null) {
                log.info("Fetched book with ID " + id + ": " + book.getTitle());
            }
            return book;
        } finally {
            em.close();
        }
    }



//    /**
//     * Retrieves books by category with pagination.
//     *
//     * @param categoryId The category ID.
//     * @param page       The page number (1-based).
//     * @return List of books in the specified category.
//     */
//    public static List<Book> getBooksByCategory(long categoryId, int page) {
//        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
//        try {
//            int firstResult = (page - 1) * PAGE_SIZE;
//            TypedQuery<Book> query = em.createQuery(
//                    "SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.category.id = :categoryId ORDER BY b.id", Book.class);
//            query.setParameter("categoryId", categoryId);
//            query.setFirstResult(firstResult);
//            query.setMaxResults(PAGE_SIZE);
//            return query.getResultList();
//        } catch (Exception e) {
//            log.log(Level.SEVERE, "Error retrieving books by category: " + categoryId, e);
//            return new ArrayList<>();
//        } finally {
//            em.close();
//        }
//    }
//
//    /**
//     * Searches for books by title.
//     *
//     * @param title The search term.
//     * @param page  The page number (1-based).
//     * @return List of books matching the search term.
//     */
//    public static List<Book> searchBooksByTitle(String title, int page) {
//        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
//        try {
//            int firstResult = (page - 1) * PAGE_SIZE;
//            String searchTerm = "%" + title.toLowerCase() + "%";
//            TypedQuery<Book> query = em.createQuery(
//                    "SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE LOWER(b.title) LIKE :title ORDER BY b.id", Book.class);
//            query.setParameter("title", searchTerm);
//            query.setFirstResult(firstResult);
//            query.setMaxResults(PAGE_SIZE);
//            return query.getResultList();
//        } catch (Exception e) {
//            log.log(Level.SEVERE, "Error searching books by title: " + title, e);
//            return new ArrayList<>();
//        } finally {
//            em.close();
//        }
//    }

    /**
     * Retrieves all top-selling books (no pagination).
     *
     * @return List of all top-selling books.
     */
    public List<Book> getTopSellingBooks() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b LEFT JOIN FETCH b.category ORDER BY b.sold DESC",
                    Book.class
            );
            query.setMaxResults(50);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving all top-selling books", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }


    /**
     * Retrieves all top-rated books (no pagination).
     *
     * @return List of all top-rated books.
     */
    public List<Book> getTopRatedBooks() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b LEFT JOIN FETCH b.category " +
                            "WHERE b.averageRating IS NOT NULL ORDER BY b.averageRating DESC",
                    Book.class
            );
            query.setMaxResults(50);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving all top-selling books", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves the newest books with pagination.
     *
     * @param page The page number (1-based).
     * @return List of newest books.
     */
    public List<Book> getNewestBooks(int page) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            int firstResult = (page - 1) * PAGE_SIZE;
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b LEFT JOIN FETCH b.category ORDER BY b.createdAt DESC", Book.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(PAGE_SIZE);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving newest books", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Adds a new book to the database.
     *
     * @param book The book to add.
     * @return true if successful, false otherwise.
     */
    public boolean addBook(Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            book.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            em.persist(book);
            em.getTransaction().commit();
            log.info("Added book: " + book.getTitle());
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.severe("Error adding book: " + e.getMessage());
            throw new RuntimeException("Error adding book", e);
        } finally {
            em.close();
        }
    }


    /**
     * Updates an existing book in the database.
     *
     * @param book The book to update.
     * @return true if successful, false otherwise.
     */
    public boolean updateBook(Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
            log.info("Updated book: " + book.getTitle());
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.severe("Error updating book: " + e.getMessage());
            throw new RuntimeException("Error updating book", e);
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param id The book ID.
     * @return true if successful, false otherwise.
     */
    public boolean deleteBook(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, (int) id);
            if (book != null) {
                em.remove(book);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error deleting book", e);
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Gets the total count of books.
     *
     * @return Total number of books.
     */
    public long getTotalBookCount() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting total book count", e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Gets the total count of books in a category.
     *
     * @param categoryId The category ID.
     * @return Total number of books in the category.
     */
    public long getTotalBookCountByCategory(long categoryId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId", Long.class);
            query.setParameter("categoryId", categoryId);
            return query.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting book count by category", e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Gets the total count of books matching a search term.
     *
     * @param title The search term.
     * @return Total number of matching books.
     */
    public long getTotalBookCountBySearch(String title) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String searchTerm = "%" + title.toLowerCase() + "%";
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(b) FROM Book b WHERE LOWER(b.title) LIKE :title", Long.class);
            query.setParameter("title", searchTerm);
            return query.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting book count by search", e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Filters books by title, publish year, and categories (include/exclude).
     *
     * @param title             The title to search (partial match).
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include (AND logic).
     * @param excludeCategories Categories to exclude.
     * @param page              The page number.
     * @return List of matching books.
     */
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
            log.info("Fetched " + result.size() + " books for filter: title=" + title + ", page=" + page);
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    /**
     * Counts total books for pagination or filtering.
     *
     * @param title             The title to search.
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include.
     * @param excludeCategories Categories to exclude.
     * @return Total number of matching books.
     */
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
            
            return query.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error counting books", e);
            return 0;
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all categories for filtering.
     *
     * @return List of categories.
     */
    public List<Category> getAllCategories() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving all categories", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Logs CSV import data to import_logs table.
     *
     * @param tableName    The table name (e.g., "books").
     * @param importedData JSON representation of imported data.
     * @return True if successful, false otherwise.
     */
    public boolean logImport(String tableName, String importedData) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("INSERT INTO import_logs (table_name, imported_data, imported_at) VALUES (?1, ?2, CURRENT_TIMESTAMP)")
                    .setParameter(1, tableName)
                    .setParameter(2, importedData)
                    .executeUpdate();
            em.getTransaction().commit();
            log.info("Logged import for table: " + tableName);
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.severe("Error logging import: " + e.getMessage());
            throw new RuntimeException("Error logging import", e);
        } finally {
            em.close();
        }
    }

    public Category getCategoryById(long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, id);
            log.info("Fetched category with ID " + id);
            return category;
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all books belonging to a specific category.
     *
     * @param categoryId The category ID.
     * @return List of books in the specified category.
     */
    public List<Book> getAllBooksByCategoryId(long categoryId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery(
                    "SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.category.id = :categoryId ORDER BY b.id",
                    Book.class);
            query.setParameter("categoryId", categoryId);
            List<Book> result = query.getResultList();
            log.info("Fetched " + result.size() + " books for category ID " + categoryId);
            return result.isEmpty() ? new ArrayList<>() : result;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error retrieving books by category ID: " + categoryId, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}