package service;

import dao.BookDao;
import model.Book;
import model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BookService {
    private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());
    private static final int PAGE_SIZE = 20;
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final BookDao bookDao = new BookDao();

    public List<Book> getAllBooks(int page) {
        return bookDao.getAllBooks(page);
    }

    public Book getBookById(long id) {
        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new RuntimeException("Book not found with ID: " + id);
        }
        return book;
    }

    public void addBook(Book book) {
        validateBook(book);
        bookDao.addBook(book);
    }

    public void updateBook(Book book) {
        validateBook(book);
        bookDao.updateBook(book);
    }

    public void deleteBook(long id) {
        bookDao.deleteBook(id);
    }

    public List<Book> filterBooks(String title, Integer publishYear, List<Long> includeCategories,
                                  List<Long> excludeCategories, int page) {
        return bookDao.filterBooks(title, publishYear, includeCategories, excludeCategories, page);
    }

    public int getTotalPages(String title, Integer publishYear, List<Long> includeCategories,
                             List<Long> excludeCategories) {
        long totalBooks = bookDao.countBooks(title, publishYear, includeCategories, excludeCategories);
        return (int) Math.ceil((double) totalBooks / PAGE_SIZE);
    }

    public List<Category> getAllCategories() {
        return bookDao.getAllCategories();
    }

    public Category getCategoryById(long categoryId) {
        Category category = bookDao.getCategoryById(categoryId);
        if (category == null) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        return category;
    }

    private void validateBook(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Validation failed: " + errorMessage);
        }
    }
}