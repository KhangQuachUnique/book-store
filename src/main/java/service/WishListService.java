package service;

import dao.WishListDao;
import model.ApiResponse;
import model.WishList;
import model.WishListItem;

import java.util.ArrayList;
import java.util.List;

public class WishListService {

    private final WishListDao wishListDao;

    public WishListService() {
        this.wishListDao = new WishListDao();
    }

    public WishList getWishListBooks(Long userId) {
        // Validate input
        if (userId == null || userId <= 0) {
            WishList empty = new WishList();
            empty.setItems(new ArrayList<>());
            empty.setUpPagination(1, 1);
            return empty;
        }

        WishList wishList = wishListDao.getWishListByUser(userId);
        if (wishList == null) {
            wishList = new WishList();
        }
        if (wishList.getItems() == null) {
            wishList.setItems(new ArrayList<>());
        }
        for (WishListItem item : wishList.getItems()) {
            item.calculateStars();
        }
        return wishList;
    }

    public WishList getWishListBooksByPage(Long userId, int currentPage, int pageSize) {
        // Guard invalid pagination params
        if (currentPage <= 0) currentPage = 1;
        if (pageSize <= 0) pageSize = 10;

        // Validate input
        if (userId == null || userId <= 0) {
            WishList empty = new WishList();
            empty.setItems(new ArrayList<>());
            empty.setUpPagination(currentPage, pageSize);
            return empty;
        }

        WishList wishList = wishListDao.getWishListByUser(userId);
        if (wishList == null) {
            wishList = new WishList();
        }
        List<WishListItem> items = wishList.getItems();
        if (items == null) {
            items = new ArrayList<>();
            wishList.setItems(items);
        }
        for (WishListItem item : items) {
            item.calculateStars();
        }
        wishList.setUpPagination(currentPage, pageSize);

        int total = items.size();
        int fromIndex = Math.min((currentPage - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        wishList.setItems(items.subList(fromIndex, toIndex));
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