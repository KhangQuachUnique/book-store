//package service;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvValidationException;
//import dao.BookDao;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import model.Book;
//import model.Category;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
///**
// * Service layer for managing book operations with caching.
// */
//public class BookService {
//    private static final int PAGE_SIZE = 20;
//    private static final Validator validator;
//
//    // Cache for books list (key: page, value: List<Book>)
//    private static final LoadingCache<Integer, List<Book>> booksCache = CacheBuilder.newBuilder()
//            .expireAfterAccess(5, TimeUnit.MINUTES)
//            .build(new CacheLoader<Integer, List<Book>>() {
//                @Override
//                public List<Book> load(Integer page) throws SQLException {
//                    return BookDao.getAllBooks(page);
//                }
//            });
//
//    // Cache for book by ID (key: id, value: Book)
//    private static final LoadingCache<Long, Book> bookByIdCache = CacheBuilder.newBuilder()
//            .expireAfterAccess(5, TimeUnit.MINUTES)
//            .build(new CacheLoader<Long, Book>() {
//                @Override
//                public Book load(Long id) throws SQLException {
//                    return BookDao.getBookById(id);
//                }
//            });
//
//    // Cache for all categories (no params, use dummy key)
//    private static final LoadingCache<String, List<Category>> categoriesCache = CacheBuilder.newBuilder()
//            .expireAfterAccess(5, TimeUnit.MINUTES)
//            .build(new CacheLoader<String, List<Category>>() {
//                @Override
//                public List<Category> load(String dummyKey) throws SQLException {
//                    return BookDao.getAllCategories();
//                }
//            });
//
//    // Cache for filtered books (key: custom string from parameters, value: List<Book>)
//    private static final LoadingCache<String, List<Book>> filterCache = CacheBuilder.newBuilder()
//            .expireAfterAccess(5, TimeUnit.MINUTES)
//            .build(new CacheLoader<String, List<Book>>() {
//                @Override
//                public List<Book> load(String filterKey) throws SQLException {
//                    String[] parts = filterKey.split("\\|");
//                    String title = parts[0].equals("null") ? null : parts[0];
//                    Integer publishYear = parts[1].equals("null") ? null : Integer.parseInt(parts[1]);
//                    List<Long> includeCategories = parseLongList(parts[2]);
//                    List<Long> excludeCategories = parseLongList(parts[3]);
//                    int page = Integer.parseInt(parts[4]);
//                    return BookDao.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
//                }
//            });
//
//    static {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    /**
//     * Retrieves all books with pagination, using cache.
//     * @param page The page number.
//     * @return List of books.
//     * @throws SQLException If a database or cache error occurs.
//     */
//    public static List<Book> getAllBooks(int page) throws SQLException {
//        try {
//            return booksCache.get(page);
//        } catch (Exception e) {
//            throw new SQLException("Error retrieving books from cache", e);
//        }
//    }
//
//    /**
//     * Retrieves a book by ID, using cache.
//     * @param id The book ID.
//     * @return The Book object or null if not found.
//     * @throws SQLException If a database or cache error occurs.
//     */
//    public static Book getBookById(long id) throws SQLException {
//        try {
//            return bookByIdCache.get(id);
//        } catch (Exception e) {
//            throw new SQLException("Error retrieving book from cache", e);
//        }
//    }
//
//    /**
//     * Adds a new book after validation.
//     * @param book The Book object.
//     * @return True if successful.
//     * @throws SQLException If a database error occurs.
//     * @throws IllegalArgumentException If validation fails.
//     */
//    public static boolean addBook(Book book) throws SQLException {
//        validateBook(book);
//        boolean success = BookDao.addBook(book);
//        if (success) {
//            invalidateCaches();
//        }
//        return success;
//    }
//
//    /**
//     * Updates an existing book after validation.
//     * @param book The Book object.
//     * @return True if successful.
//     * @throws SQLException If a database error occurs.
//     * @throws IllegalArgumentException If validation fails.
//     */
//    public static boolean updateBook(Book book) throws SQLException {
//        validateBook(book);
//        boolean success = BookDao.updateBook(book);
//        if (success) {
//            invalidateCaches();
//        }
//        return success;
//    }
//
//    /**
//     * Deletes a book by ID.
//     * @param id The book ID.
//     * @return True if successful.
//     * @throws SQLException If a database error occurs.
//     */
//    public static boolean deleteBook(long id) throws SQLException {
//        boolean success = BookDao.deleteBook(id);
//        if (success) {
//            invalidateCaches();
//        }
//        return success;
//    }
//
//    /**
//     * Filters books by criteria, using cache.
//     * @param title The title to search.
//     * @param publishYear The publication year.
//     * @param includeCategories Categories to include.
//     * @param excludeCategories Categories to exclude.
//     * @param page The page number.
//     * @return List of matching books.
//     * @throws SQLException If a database or cache error occurs.
//     */
//    public static List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
//                                         List<Long> excludeCategories, int page) throws SQLException {
//        String filterKey = (title == null ? "null" : title) + "|" +
//                (publishYear == null ? "null" : publishYear.toString()) + "|" +
//                (includeCategories == null ? "null" : includeCategories.toString()) + "|" +
//                (excludeCategories == null ? "null" : excludeCategories.toString()) + "|" + page;
//        try {
//            return filterCache.get(filterKey);
//        } catch (Exception e) {
//            throw new SQLException("Error retrieving filtered books from cache", e);
//        }
//    }
//
//    /**
//     * Calculates total pages for pagination.
//     * @param title The title to search.
//     * @param publishYear The publication year.
//     * @param includeCategories Categories to include.
//     * @param excludeCategories Categories to exclude.
//     * @return Total number of pages.
//     * @throws SQLException If a database error occurs.
//     */
//    public static int getTotalPages(String title, Integer publishYear, List<Long> includeCategories,
//                                    List<Long> excludeCategories) throws SQLException {
//        long totalBooks = BookDao.countBooks(title, publishYear, includeCategories, excludeCategories);
//        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
//        return Math.max(1, totalPages); // Đảm bảo luôn có ít nhất 1 trang
//    }
//
//    /**
//     * Retrieves all categories, using cache.
//     * @return List of categories.
//     * @throws SQLException If a database or cache error occurs.
//     */
//    public static List<Category> getAllCategories() throws SQLException {
//        try {
//            return categoriesCache.get("dummy");
//        } catch (Exception e) {
//            throw new SQLException("Error retrieving categories from cache", e);
//        }
//    }
//
//    /**
//     * Imports books from a CSV file.
//     * @param csvStream The CSV file input stream.
//     * @return Error message if validation fails, null if successful.
//     * @throws SQLException If a database error occurs.
//     * @throws IOException If an I/O error occurs.
//     * @throws CsvValidationException If CSV parsing fails.
//     */
//    public static String importBooksFromCSV(InputStream csvStream)
//            throws SQLException, IOException, CsvValidationException {
//        List<Book> books = new ArrayList<>();
//        StringBuilder errors = new StringBuilder();
//        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream))) {
//            String[] headers = reader.readNext();
//            if (!validateCSVHeaders(headers)) {
//                return "Invalid CSV format: Incorrect headers";
//            }
//
//            String[] line;
//            int lineNumber = 1;
//            while ((line = reader.readNext()) != null) {
//                lineNumber++;
//                try {
//                    Book book = parseCSVLine(line);
//                    validateBook(book);
//                    books.add(book);
//                } catch (Exception e) {
//                    errors.append("Error at line ").append(lineNumber).append(": ").append(e.getMessage()).append("\n");
//                }
//            }
//
//            if (errors.length() > 0) {
//                return errors.toString();
//            }
//
//            for (Book book : books) {
//                BookDao.addBook(book);
//            }
//            BookDao.logImport("books", new com.google.gson.Gson().toJson(books));
//            invalidateCaches();
//            return null;
//        }
//    }
//
//    /**
//     * Validates CSV headers.
//     * @param headers The CSV headers.
//     * @return True if headers are valid.
//     */
//    private static boolean validateCSVHeaders(String[] headers) {
//        String[] expected = { "title", "author", "publisher", "category_id", "stock", "original_price",
//                "discount_rate", "thumbnail_url", "description", "publish_year", "pages",
//                "rating_average", "price" };
//        if (headers.length != expected.length)
//            return false;
//        for (int i = 0; i < headers.length; i++) {
//            if (!headers[i].equalsIgnoreCase(expected[i]))
//                return false;
//        }
//        return true;
//    }
//
//    /**
//     * Parses a CSV line into a Book object.
//     * @param line The CSV line.
//     * @return The Book object.
//     * @throws Exception If parsing fails.
//     */
//    private static Book parseCSVLine(String[] line) throws Exception {
//        if (line.length != 13)
//            throw new Exception("Invalid number of columns");
//        Book book = new Book();
//        book.setTitle(line[0]);
//        book.setAuthor(line[1]);
//        book.setPublisher(line[2]);
//        book.setCategoryId(Integer.parseInt(line[3]));
//        book.setStock(Integer.parseInt(line[4]));
//        book.setOriginalPrice(Double.parseDouble(line[5]));
//        book.setDiscount_rate(Integer.parseInt(line[6]));
//        book.setThumbnailUrl(line[7]);
//        book.setDescription(line[8]);
//        book.setPublishYear(line[9].isEmpty() ? null : Integer.parseInt(line[9]));
//        book.setPages(line[10].isEmpty() ? null : Integer.parseInt(line[10]));
//        book.setRating(Double.parseDouble(line[11]));
//        book.setPrice(Double.parseDouble(line[12]));
//        return book;
//    }
//
//    /**
//     * Validates a Book object using Bean Validation and custom rules.
//     * @param book The Book object.
//     * @throws IllegalArgumentException If validation fails.
//     */
//    private static void validateBook(Book book) throws IllegalArgumentException {
//        Set<ConstraintViolation<Book>> violations = validator.validate(book);
//        if (!violations.isEmpty()) {
//            StringBuilder errors = new StringBuilder();
//            for (ConstraintViolation<Book> violation : violations) {
//                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
//            }
//            throw new IllegalArgumentException(errors.toString());
//        }
//
//        if (book.getTitle() == null || book.getTitle().isEmpty()) {
//            throw new IllegalArgumentException("Title is required");
//        }
//        if (book.getPrice() < 0) {
//            throw new IllegalArgumentException("Price must be non-negative");
//        }
//        if (book.getOriginalPrice() < 0) {
//            throw new IllegalArgumentException("Original price must be non-negative");
//        }
//        if (book.getDiscount_rate() < 0 || book.getDiscount_rate() > 100) {
//            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
//        }
//        if (book.getStock() < 0) {
//            throw new IllegalArgumentException("Stock must be non-negative");
//        }
//    }
//
//    /**
//     * Invalidates all caches after data modification.
//     */
//    private static void invalidateCaches() {
//        booksCache.invalidateAll();
//        bookByIdCache.invalidateAll();
//        categoriesCache.invalidateAll();
//        filterCache.invalidateAll();
//    }
//
//    /**
//     * Parses a string of comma-separated longs into a List<Long>.
//     * @param str The input string.
//     * @return List of longs.
//     */
//    private static List<Long> parseLongList(String str) {
//        if (str == null || str.equals("null") || str.isEmpty()) {
//            return new ArrayList<>();
//        }
//        str = str.replaceAll("[\\[\\] ]", "");
//        if (str.isEmpty()) {
//            return new ArrayList<>();
//        }
//        return Arrays.stream(str.split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//    }
//}