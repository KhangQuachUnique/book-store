package service;

import java.util.List;
import dao.CategoryBookDao;
import model.Book;

public class CategoryBookService {
    public static List<Book> getBooksByCategory(int categoryId, int page) {
        return CategoryBookDao.getBooksByCategoryId(categoryId, page);
    }

    public static List<Book> getAllBooks(int page) {
        return CategoryBookDao.getAllBook(page);
    }

    public static int getTotalPages() {
        return CategoryBookDao.getTotalPages();
    }

    public static int getTotalPagesByCategory(int categoryId) {
        return CategoryBookDao.getTotalPagesByCategory(categoryId);
    }
}
