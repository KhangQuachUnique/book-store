package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import dao.BookDao;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import model.Book;
import model.Category;

/**
 * Service layer for managing book operations.
 */
public class BookService {
    private static final int PAGE_SIZE = 20;
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Retrieves all books with pagination.
     *
     * @param page The page number.
     * @return List of books.
     * @throws SQLException If a database error occurs.
     */
    public static List<Book> getAllBooks(int page) throws SQLException {
        return BookDao.getAllBooks(page);
    }

    /**
     * Retrieves a book by ID.
     *
     * @param id The book ID.
     * @return The Book object or null if not found.
     * @throws SQLException If a database error occurs.
     */
    public static Book getBookById(long id) throws SQLException {
        return BookDao.getBookById(id);
    }

    /**
     * Adds a new book after validation.
     *
     * @param book The Book object.
     * @return True if successful.
     * @throws SQLException             If a database error occurs.
     * @throws IllegalArgumentException If validation fails.
     */
    public static boolean addBook(Book book) throws SQLException {
        validateBook(book);
        return BookDao.addBook(book);
    }

    /**
     * Updates an existing book after validation.
     *
     * @param book The Book object.
     * @return True if successful.
     * @throws SQLException             If a database error occurs.
     * @throws IllegalArgumentException If validation fails.
     */
    public static boolean updateBook(Book book) throws SQLException {
        validateBook(book);
        return BookDao.updateBook(book);
    }

    /**
     * Deletes a book by ID.
     *
     * @param id The book ID.
     * @return True if successful.
     * @throws SQLException If a database error occurs.
     */
    public static boolean deleteBook(long id) throws SQLException {
        return BookDao.deleteBook(id);
    }

    /**
     * Filters books by criteria.
     *
     * @param title             The title to search.
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include.
     * @param excludeCategories Categories to exclude.
     * @param page              The page number.
     * @return List of matching books.
     * @throws SQLException If a database error occurs.
     */
    public static List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
            List<Long> excludeCategories, int page) throws SQLException {
        return BookDao.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
    }

    /**
     * Calculates total pages for pagination.
     *
     * @param title             The title to search.
     * @param publishYear       The publication year.
     * @param includeCategories Categories to include.
     * @param excludeCategories Categories to exclude.
     * @return Total number of pages.
     * @throws SQLException If a database error occurs.
     */
    public static int getTotalPages(String title, Integer publishYear, List<Long> includeCategories,
            List<Long> excludeCategories) throws SQLException {
        long totalBooks = BookDao.countBooks(title, publishYear, includeCategories, excludeCategories);
        return (int) Math.ceil((double) totalBooks / PAGE_SIZE);
    }

    /**
     * Retrieves all categories.
     *
     * @return List of categories.
     * @throws SQLException If a database error occurs.
     */
    public static List<Category> getAllCategories() throws SQLException {
        return BookDao.getAllCategories();
    }

    /**
     * Imports books from a CSV file.
     *
     * @param csvStream The CSV file input stream.
     * @return Error message if validation fails, null if successful.
     * @throws SQLException           If a database error occurs.
     * @throws IOException            If an I/O error occurs.
     * @throws CsvValidationException If CSV parsing fails.
     */
    public static String importBooksFromCSV(InputStream csvStream)
            throws SQLException, IOException, CsvValidationException {
        List<Book> books = new ArrayList<>();
        StringBuilder errors = new StringBuilder();
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream))) {
            String[] headers = reader.readNext();
            if (!validateCSVHeaders(headers)) {
                return "Invalid CSV format: Incorrect headers";
            }

            String[] line;
            int lineNumber = 1;
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    Book book = parseCSVLine(line);
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
                BookDao.addBook(book);
            }
            BookDao.logImport("books", new com.google.gson.Gson().toJson(books));
            return null;
        }
    }

    /**
     * Validates CSV headers.
     *
     * @param headers The CSV headers.
     * @return True if headers are valid.
     */
    private static boolean validateCSVHeaders(String[] headers) {
        String[] expected = { "title", "author", "publisher", "category_id", "stock", "original_price",
                "discount_rate", "thumbnail_url", "description", "publish_year", "pages",
                "rating_average", "price" };
        if (headers.length != expected.length)
            return false;
        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].equalsIgnoreCase(expected[i]))
                return false;
        }
        return true;
    }

    /**
     * Parses a CSV line into a Book object.
     *
     * @param line The CSV line.
     * @return The Book object.
     * @throws Exception If parsing fails.
     */
    private static Book parseCSVLine(String[] line) throws Exception {
        if (line.length != 13)
            throw new Exception("Invalid number of columns");
        Book book = new Book();
        book.setTitle(line[0]);
        book.setAuthor(line[1]);
        book.setPublisher(line[2]);
        book.setCategoryId(Integer.parseInt(line[3]));
        book.setStock(Integer.parseInt(line[4]));
        book.setOriginalPrice(Double.parseDouble(line[5]));
        book.setDiscount_rate(Integer.parseInt(line[6]));
        book.setThumbnailUrl(line[7]);
        book.setDescription(line[8]);
        book.setPublishYear(line[9].isEmpty() ? null : Integer.parseInt(line[9]));
        book.setPages(line[10].isEmpty() ? null : Integer.parseInt(line[10]));
        book.setRating(Double.parseDouble(line[11]));
        book.setPrice(Double.parseDouble(line[12]));
        return book;
    }

    /**
     * Validates a Book object using Bean Validation and custom rules.
     *
     * @param book The Book object.
     * @throws IllegalArgumentException If validation fails.
     */
    private static void validateBook(Book book) throws IllegalArgumentException {
        // Bean Validation
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<Book> violation : violations) {
                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errors.toString());
        }

        // Custom validation
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (book.getOriginalPrice() < 0) {
            throw new IllegalArgumentException("Original price must be non-negative");
        }
        if (book.getDiscount_rate() < 0 || book.getDiscount_rate() > 100) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        }
        if (book.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be non-negative");
        }
    }
}
