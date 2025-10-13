package service;

import dao.WishListDao;
import model.ApiResponse;
import model.WishList;
import model.WishListItem;

import java.util.ArrayList;

public class WishListService {

    private final WishListDao wishListDao;

    public WishListService() {
        this.wishListDao = new WishListDao();
    }

    public WishList getWishListBooks(Long userId) {
        WishList wishList = wishListDao.getWishListByUser(userId);
        if (wishList == null) {
            wishList = new WishList();
            wishList.setItems(new ArrayList<>());
        }
        if (wishList.getItems() != null) {
            for (WishListItem item : wishList.getItems()) {
                item.calculateStars();
            }
        }
        return wishList;
    }

    public WishList getWishListBooksByPage(Long userId, int currentPage, int pageSize) {
        WishList wishList = wishListDao.getWishListByUser(userId);
        if (wishList == null) {
            wishList = new WishList();
            wishList.setItems(new ArrayList<>());
        }
        if (wishList.getItems() != null) {
            for (WishListItem item : wishList.getItems()) {
                item.calculateStars();
            }
        }
        wishList.setUpPagination(currentPage, pageSize);
        int size = wishList.getItems() == null ? 0 : wishList.getItems().size();
        int fromIndex = Math.min((currentPage - 1) * pageSize, size);
        int toIndex = Math.min(fromIndex + pageSize, size);
        if (wishList.getItems() != null) {
            wishList.setItems(wishList.getItems().subList(fromIndex, toIndex));
        }
        return wishList;
    }

    //Add book to wish list
    public ApiResponse<Void> addBookToWishList(Long userId, Long bookId) {
        if (userId == null || bookId == null || userId <= 0 || bookId <= 0) {
            return new ApiResponse<>(false, "ID không hợp lệ", null);
        }

        boolean result = wishListDao.addBookToWishlist(userId, bookId);

        if (result) {
            return new ApiResponse<>(true, "Thêm sách vào danh sách yêu thích thành công", null);
        } else {
            return new ApiResponse<>(false, "Thêm sách vào danh sách yêu thích thất bại", null);
        }
    }

    //Remove book from wish list
    public ApiResponse<Void> removeBookToWishList(Long userId, Long bookId) {
        if (userId == null || bookId == null || userId <= 0 || bookId <= 0) {
            return new ApiResponse<>(false, "ID không hợp lệ", null);
        }

        boolean success = wishListDao.removeBookFromWishlist(userId, bookId);
        if (success) {
            return new ApiResponse<>(true, "Xóa sách khỏi danh sách yêu thích thành công", null);
        } else {
            return new ApiResponse<>(false, "Xóa sách khỏi danh sách yêu thích thất bại", null);
        }
    }
}