package service;

import model.Book;

import java.util.List;

public class WishListService {
    public static List<Book> getWhiteListBooks(int userId) {
        return dao.WhiteListDao.getWhiteListBooks(userId);
    }
}
