package service;

import model.Review;

import java.util.List;

public class ReviewService {
    public static List<Review> getReviewsByBookId(int bookId) {
        return dao.ReviewDao.getReviewsByBookId(bookId);
    }
}
