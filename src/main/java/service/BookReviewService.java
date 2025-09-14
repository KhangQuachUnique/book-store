package service;

import dao.BookReviewDao;
import model.BookReview;

public class BookReviewService {
    public static BookReview getReviewsByBookId(int bookId) {
        return BookReviewDao.getReviewsByBookId(bookId);
    }
}
