package service;

import model.Book;

import java.util.List;

public class CategoryBookService {
    public static List<Book> getBooksByCategory(int categoryId) {
        return dao.CategoryBookDao.getBooksByCategoryId(categoryId);
    }
}
