package service;

import java.util.List;
import java.util.Optional;
import dao.BookDao;
import model.Book;

public class BookService {
    private final BookDao bookDao;

    public BookService() {
        this.bookDao = new BookDao();
    }

    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public Book getBookById(long id) {
        Optional<Book> book = bookDao.getBookById(id);
        return book.orElse(null);
    }
}
