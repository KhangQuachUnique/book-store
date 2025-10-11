package service;

import dao.WishListDao;
import model.ApiResponse;
import model.WishList;
import model.WishListItem;

public class WishListService {

    private final WishListDao wishListDao;

    public WishListService() {
        this.wishListDao = new WishListDao();
    }

    public WishList getWishListBooks(Long userId, int currentPage, int pageSize) {
        WishList wishList = wishListDao.getWishListByUser(userId);
        for (WishListItem item : wishList.getItems()) {
            item.calculateStars();
        }
        wishList.setUpPagination(currentPage, pageSize);
        int fromIndex = Math.min((currentPage - 1) * pageSize, wishList.getItems().size());
        int toIndex = Math.min(fromIndex + pageSize, wishList.getItems().size());
        wishList.setItems(wishList.getItems().subList(fromIndex, toIndex));
        return wishList;
    }

    //Add book to wish list
    public ApiResponse addBookToWishList(Long userId, Long bookId) {
        if (userId <= 0 || bookId <= 0) {
            return new ApiResponse(false, "ID không hợp lệ", null);
        }

        boolean result = wishListDao.addBookToWishlist(userId, bookId);

        if (result) {
            return new ApiResponse(true, "Thêm sách vào danh sách yêu thích thành công", null);
        } else {
            return new ApiResponse(false, "Thêm sách vào danh sách yêu thích thất bại", null);
        }
    }

    //Remove book from wish list
    public ApiResponse removeBookToWishList(Long userId, Long bookId) {
        if (userId <= 0 || bookId <= 0) {
            return new ApiResponse(false, "ID không hợp lệ", null);
        }

        boolean success = wishListDao.removeBookFromWishlist(userId, bookId);
        if (success) {
            return new ApiResponse(true, "Xóa sách khỏi danh sách yêu thích thành công", null);
        } else {
            return new ApiResponse(false, "Xóa sách khỏi danh sách yêu thích thất bại", null);
        }
    }
}