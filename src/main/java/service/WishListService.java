package service;

import dao.WishListDao;
import model.ApiResponse;

public class WishListService {
    //Get wish list books by user id
    public static ApiResponse getWishListBooks(int userId) {
        if (userId <= 0) {
            return new ApiResponse(false, "ID không hợp lệ", null);
        }

        return new ApiResponse(true, "Lấy danh sách yêu thích thành công", WishListDao.getWishListBooks(userId));
    }

    //Add book to wish list
    public static ApiResponse addBookToWishList(int userId, int bookId) {
        if (userId <= 0 || bookId <= 0) {
            return new ApiResponse(false, "ID không hợp lệ", null);
        }

        String result = WishListDao.addBookToWishList(userId, bookId);
        if ("success".equals(result)) {
            return new ApiResponse(true, "Thêm sách vào danh sách yêu thích thành công", null);
        } else if ("exists".equals(result)) {
            return new ApiResponse(false, "Sách đã có trong danh sách yêu thích", null);
        } else {
            return new ApiResponse(false, "Thêm sách vào danh sách yêu thích thất bại", null);
        }
    }

    //Remove book from wish list
    public static  ApiResponse removeBookToWishList(int userId, int bookId) {
        if (userId <= 0 || bookId <= 0) {
            return new ApiResponse(false, "ID không hợp lệ", null);
        }

        boolean success = WishListDao.deleteBookFromWishList(userId, bookId);
        if (success) {
            return new ApiResponse(true, "Xóa sách khỏi danh sách yêu thích thành công", null);
        } else {
            return new ApiResponse(false, "Xóa sách khỏi danh sách yêu thích thất bại", null);
        }
    }
}