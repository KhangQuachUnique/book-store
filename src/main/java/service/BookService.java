package service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import dao.BookDao;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import model.Book;
import model.Category;
import util.JPAUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookService {
    private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());
    private static final int PAGE_SIZE = 20;
    private static final Validator validator;

    private static final LoadingCache<Integer, List<Book>> booksCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, List<Book>>() {
                @Override
                public List<Book> load(Integer page) {
                    List<Book> books = BookDao.getAllBooks(page);
                    LOGGER.info("Loaded " + books.size() + " books for page " + page);
                    return books;
                }
            });

    public static Category getCategoryById(long id) throws RuntimeException {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, (int) id);
            LOGGER.info("Fetched category with ID " + id + ": " + (category != null ? category.getName() : "null"));
            return category;
        } finally {
            em.close();
        }
    }

    private static final LoadingCache<Long, Book> bookByIdCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, Book>() {
                @Override
                public Book load(Long id) {
                    Book book = BookDao.getBookById(id);
                    LOGGER.info("Loaded book with ID " + id + ": " + (book != null ? book.getTitle() : "null"));
                    return book;
                }
            });

    private static final LoadingCache<String, List<Category>> categoriesCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<Category>>() {
                @Override
                public List<Category> load(String dummyKey) {
                    List<Category> categories = BookDao.getAllCategories();
                    LOGGER.info("Loaded " + categories.size() + " categories");
                    return categories;
                }
            });

    private static final LoadingCache<String, List<Book>> filterCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List<Book>>() {
                @Override
                public List<Book> load(String filterKey) {
                    String[] parts = filterKey.split("\\|");
                    String title = parts[0].equals("null") ? null : parts[0];
                    Integer publishYear = parts[1].equals("null") ? null : Integer.parseInt(parts[1]);
                    List<Long> includeCategories = parseLongList(parts[2]);
                    List<Long> excludeCategories = parseLongList(parts[3]);
                    int page = Integer.parseInt(parts[4]);
                    List<Book> books = BookDao.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
                    LOGGER.info("Loaded " + books.size() + " books for filter: " + filterKey);
                    return books;
                }
            });

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static List<Book> getAllBooks(int page) throws RuntimeException {
        try {
            List<Book> books = booksCache.get(page);
            return books != null ? books : new ArrayList<>();
        } catch (Exception e) {
            LOGGER.severe("Error retrieving books from cache: " + e.getMessage());
            throw new RuntimeException("Error retrieving books from cache", e);
        }
    }

    public static Book getBookById(long id) throws RuntimeException {
        try {
            return bookByIdCache.get(id);
        } catch (Exception e) {
            LOGGER.severe("Error retrieving book from cache: " + e.getMessage());
            throw new RuntimeException("Error retrieving book from cache", e);
        }
    }

    public static boolean addBook(Book book) throws RuntimeException {
        validateBook(book);
        boolean success = BookDao.addBook(book);
        if (success) {
            invalidateCaches();
            LOGGER.info("Book added: " + book.getTitle());
        }
        return success;
    }

    public static boolean updateBook(Book book) throws RuntimeException {
        validateBook(book);
        boolean success = BookDao.updateBook(book);
        if (success) {
            invalidateCaches();
            LOGGER.info("Book updated: " + book.getTitle());
        }
        return success;
    }

    public static boolean deleteBook(long id) throws RuntimeException {
        boolean success = BookDao.deleteBook(id);
        if (success) {
            invalidateCaches();
            LOGGER.info("Book deleted: ID " + id);
        }
        return success;
    }

    public static List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
                                         List<Long> excludeCategories, int page) throws RuntimeException {
        String filterKey = (title == null ? "null" : title) + "|" +
                (publishYear == null ? "null" : publishYear.toString()) + "|" +
                (includeCategories == null ? "null" : includeCategories.toString()) + "|" +
                (excludeCategories == null ? "null" : excludeCategories.toString()) + "|" + page;
        try {
            List<Book> books = filterCache.get(filterKey);
            return books != null ? books : new ArrayList<>();
        } catch (Exception e) {
            LOGGER.severe("Error retrieving filtered books from cache: " + e.getMessage());
            throw new RuntimeException("Error retrieving filtered books from cache", e);
        }
    }

    public static int getTotalPages(String title, Integer publishYear, List<Long> includeCategories,
                                    List<Long> excludeCategories) throws RuntimeException {
        try {
            long totalBooks = BookDao.countBooks(title, publishYear, includeCategories, excludeCategories);
            int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
            LOGGER.info("Total pages calculated: " + totalPages + " for filter title=" + title + ", publishYear=" + publishYear);
            return Math.max(1, totalPages);
        } catch (Exception e) {
            LOGGER.severe("Error calculating total pages: " + e.getMessage());
            throw new RuntimeException("Error calculating total pages", e);
        }
    }

    public static List<Category> getAllCategories() throws RuntimeException {
        try {
            List<Category> categories = categoriesCache.get("dummy");
            return categories != null ? categories : new ArrayList<>();
        } catch (Exception e) {
            LOGGER.severe("Error retrieving categories from cache: " + e.getMessage());
            throw new RuntimeException("Error retrieving categories from cache", e);
        }
    }

    public static String importBooksFromCSV(InputStream csvStream)
            throws RuntimeException, IOException, CsvValidationException {
        List<Book> books = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream))) {
            String[] headers = reader.readNext();
            if (!validateCSVHeaders(headers)) {
                return "Invalid CSV format: Incorrect headers";
            }

            String[] line;
            int lineNumber = 1;
            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
            try {
                em.getTransaction().begin();
                while ((line = reader.readNext()) != null) {
                    lineNumber++;
                    try {
                        Book book = parseCSVLine(line, em);
                        validateBook(book);
                        books.add(book);
                    } catch (Exception e) {
                        errors.append("Error at line ").append(lineNumber).append(": ").append(e.getMessage()).append("\n");
                    }
                }

                if (errors.length() > 0) {
                    return errors.toString();
                }

                for (Book book : books) {
                    book.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
                    em.persist(book);
                }
                em.createNativeQuery("INSERT INTO import_logs (table_name, imported_data, imported_at) VALUES (?1, ?2, CURRENT_TIMESTAMP)")
                        .setParameter(1, "books")
                        .setParameter(2, new com.google.gson.Gson().toJson(books))
                        .executeUpdate();
                em.getTransaction().commit();
                invalidateCaches();
                LOGGER.info("Imported " + books.size() + " books from CSV");
                return null;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                LOGGER.severe("Error importing books: " + e.getMessage());
                throw new RuntimeException("Error importing books", e);
            } finally {
                em.close();
            }
        }
    }

    private static boolean validateCSVHeaders(String[] headers) {
        String[] expected = { "title", "author", "publisher", "categoryId", "stock", "originalPrice",
                "discountRate", "thumbnailUrl", "description", "publishYear", "pages",
                "averageRating", "price", "sold" };
        if (headers.length != expected.length)
            return false;
        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].equalsIgnoreCase(expected[i]))
                return false;
        }
        return true;
    }

    private static Book parseCSVLine(String[] line, EntityManager em) throws Exception {
        if (line.length != 14)
            throw new Exception("Invalid number of columns");
        Book book = new Book();
        book.setTitle(line[0]);
        book.setAuthor(line[1]);
        book.setPublisher(line[2]);
        int categoryId = Integer.parseInt(line[3]);
        Category category = em.find(Category.class, categoryId);
        if (category == null) {
            throw new Exception("Invalid category ID: " + line[3]);
        }
        book.setCategoryId(categoryId);
        book.setStock(Integer.parseInt(line[4]));
        book.setOriginalPrice(Double.parseDouble(line[5]));
        book.setDiscountRate(Integer.parseInt(line[6]));
        book.setThumbnailUrl(line[7]);
        book.setDescription(line[8]);
        book.setPublishYear(line[9].isEmpty() ? null : Integer.parseInt(line[9]));
        book.setPages(line[10].isEmpty() ? null : Integer.parseInt(line[10]));
        book.setAverageRating(line[11].isEmpty() ? null : Double.parseDouble(line[11]));
        book.setPrice(Double.parseDouble(line[12]));
        book.setSold(line[13].isEmpty() ? 0 : Integer.parseInt(line[13]));
        return book;
    }

    private static void validateBook(Book book) throws IllegalArgumentException {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<Book> violation : violations) {
                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errors.toString());
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (book.getOriginalPrice() < 0) {
            throw new IllegalArgumentException("Original price must be non-negative");
        }
        if (book.getDiscountRate() < 0 || book.getDiscountRate() > 100) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        }
        if (book.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be non-negative");
        }
        if (book.getSold() < 0) {
            throw new IllegalArgumentException("Sold must be non-negative");
        }
    }

    private static void invalidateCaches() {
        booksCache.invalidateAll();
        bookByIdCache.invalidateAll();
        categoriesCache.invalidateAll();
        filterCache.invalidateAll();
        LOGGER.info("All caches invalidated");
    }

    private static List<Long> parseLongList(String str) {
        if (str == null || str.equals("null") || str.isEmpty()) {
            return new ArrayList<>();
        }
        str = str.replaceAll("[\\[\\] ]", "");
        if (str.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}