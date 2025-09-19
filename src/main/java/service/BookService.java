package service;

import java.util.List;

import dao.BookDao;
import model.Book;

public class BookService {
    public static List<Book> getAllBooks() {

        return BookDao.getAllBooks();
    }

    public static Book getBookById(int id) {
        return BookDao.getBookById(id);
    }
}
