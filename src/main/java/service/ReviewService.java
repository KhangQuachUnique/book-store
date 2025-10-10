package service;

import dao.ReviewDao;
import model.ApiResponse;
import model.BookReview;
import model.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService() {
        this.reviewDao = new ReviewDao();
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

    public ApiResponse likeReview(Long reviewId, Long userId) {
        try {
            boolean success = reviewDao.likeReview(reviewId, userId);
            if (success) {
                return new ApiResponse(true, "Review liked successfully.", null);
            } else {
                return new ApiResponse(false, "Failed to like the review.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, "An error occurred while liking the review.", null);
        }
    }

    public ApiResponse dislikeReview(Long reviewId, Long userId) {
        try {
            boolean success = reviewDao.dislikeReview(reviewId, userId);
            if (success) {
                return new ApiResponse(true, "Review unliked successfully.", null);
            } else {
                return new ApiResponse(false, "Failed to unlike the review.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(false, "An error occurred while unliking the review.", null);
        }
    }
}
