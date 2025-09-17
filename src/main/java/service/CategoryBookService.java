package service;

import dao.CategoryBookDao;
import model.Book;

import java.util.List;

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
