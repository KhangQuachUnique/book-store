package service;

import dao.BookReviewDao;
import model.ApiResponse;
import model.BookReview;
import model.Review;

public class BookReviewService {
    public static BookReview getReviewsByBookId(int bookId) {
        BookReview bookReview = BookReviewDao.getReviewsByBookId(bookId);
        Integer fiveStarCount = 0;
        Integer fourStarCount = 0;
        Integer threeStarCount = 0;
        Integer twoStarCount = 0;
        Integer oneStarCount = 0;
        if (bookReview != null && bookReview.getReviews() != null) {
            for (Review review : bookReview.getReviews()) {
                switch (review.getRating().intValue()) {
                    case 5:
                        fiveStarCount++;
                        break;
                    case 4:
                        fourStarCount++;
                        break;
                    case 3:
                        threeStarCount++;
                        break;
                    case 2:
                        twoStarCount++;
                        break;
                    case 1:
                        oneStarCount++;
                        break;
                }
            }
            bookReview.setFiveStarCount(fiveStarCount);
            bookReview.setFourStarCount(fourStarCount);
            bookReview.setThreeStarCount(threeStarCount);
            bookReview.setTwoStarCount(twoStarCount);
            bookReview.setOneStarCount(oneStarCount);
        }
        return bookReview;
    }

    public static ApiResponse likeReview(int reviewId) {
        try {
            boolean success = BookReviewDao.likeReview(reviewId);
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

    public static ApiResponse unlikeReview(int reviewId) {

        try {
            boolean success = BookReviewDao.unlikeReview(reviewId);
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
