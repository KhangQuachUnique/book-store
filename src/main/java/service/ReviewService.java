package service;

import dao.OrderDAO;
import dao.ReviewDao;
import model.ApiResponse;
import model.BookReview;
import model.Review;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReviewService {

    private final ReviewDao reviewDao;
    private final OrderDAO orderDAO;

    public ReviewService() {
        this.reviewDao = new ReviewDao();
        this.orderDAO = new OrderDAO();
    }

    public BookReview getReviewsByBookId(Long bookId, Long currentUserId) {
        List<Review> reviews = reviewDao.getReviewsByBookId(bookId, currentUserId);

        double totalRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();

        double averageRating = reviews.isEmpty() ? 0.0 : totalRating / reviews.size();

        BookReview bookReview = new BookReview();
        bookReview.setReviews(reviews);
        bookReview.setAverageRating(averageRating);
        bookReview.setTotalReviews(reviews.size());

        bookReview.calculateStars();
        bookReview.calculateStarCounts();

        return bookReview;
    }

    public ApiResponse<Void> likeReview(Long reviewId, Long userId) {
        try {
            boolean success = reviewDao.likeReview(reviewId, userId);
            if (success) {
                return new ApiResponse<>(true, "Review liked successfully.", null);
            } else {
                return new ApiResponse<>(false, "Failed to like the review.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "An error occurred while liking the review.", null);
        }
    }

    public ApiResponse<Void> dislikeReview(Long reviewId, Long userId) {
        try {
            boolean success = reviewDao.dislikeReview(reviewId, userId);
            if (success) {
                return new ApiResponse<>(true, "Review unliked successfully.", null);
            } else {
                return new ApiResponse<>(false, "Failed to unlike the review.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "An error occurred while unliking the review.", null);
        }
    }

    // Check if user already reviewed a book
    public boolean hasUserReviewedBook(Long userId, Long bookId) {
        return reviewDao.hasUserReviewedBook(userId, bookId);
    }

    // Get set of reviewed book IDs (Integer) among a candidate list
    public Set<Integer> getReviewedBookIds(Long userId, List<Integer> bookIds) {
        if (userId == null || bookIds == null || bookIds.isEmpty()) return new HashSet<>();
        return reviewDao.getReviewedBookIds(userId, bookIds);
    }

    // New: add review once with purchase check
    public ApiResponse<Void> addOrUpdateReview(Long userId, Long bookId, Integer rating, String comment) {
        try {
            if (userId == null || bookId == null || rating == null || rating < 1 || rating > 5) {
                return new ApiResponse<>(false, "Dữ liệu không hợp lệ", null);
            }
            boolean purchased = orderDAO.userPurchasedBookDelivered(userId, bookId);
            if (!purchased) {
                return new ApiResponse<>(false, "Bạn chỉ có thể đánh giá sản phẩm đã mua và đã giao.", null);
            }
            if (reviewDao.hasUserReviewedBook(userId, bookId)) {
                return new ApiResponse<>(false, "Bạn đã đánh giá sản phẩm này rồi.", null);
            }
            boolean ok = reviewDao.addOrUpdateReview(userId, bookId, rating, comment);
            if (ok) {
                return new ApiResponse<>(true, "Đánh giá thành công", null);
            }
            return new ApiResponse<>(false, "Không thể lưu đánh giá", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Lỗi hệ thống khi lưu đánh giá", null);
        }
    }
}
