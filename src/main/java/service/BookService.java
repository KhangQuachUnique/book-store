package service;

import model.Book;
import dao.BookDao;
import java.util.List;

public class  BookService {
    public static List<Book> getAllBooks() {

        return BookDao.getAllBooks();
    }

    public static Book getBookById(int id) {
        return BookDao.getBookById(id);
    }
}
