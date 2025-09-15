package service;

import dao.BookReviewDao;
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
}
