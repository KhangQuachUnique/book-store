package service;

import model.Book;
import java.util.List;

public class WishListService {
    public static List<Book> getWishListBooks(int userId) {
        return dao.WishListDao.getWishListBooks(userId);
    }
}
