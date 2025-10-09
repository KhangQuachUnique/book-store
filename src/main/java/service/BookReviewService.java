//package service;
//
//import model.ApiResponse;
//import model.BookReview;
//import model.ReviewShow;
//
//public class BookReviewService {
//    public static BookReview getReviewsByBookId(long bookId, long currentUserId) {
//        BookReview bookReview = BookReviewDao.getReviewsByBookId(bookId, currentUserId);
//        Integer fiveStarCount = 0;
//        Integer fourStarCount = 0;
//        Integer threeStarCount = 0;
//        Integer twoStarCount = 0;
//        Integer oneStarCount = 0;
//        if (bookReview != null && bookReview.getReviewShows() != null) {
//            for (ReviewShow reviewShow : bookReview.getReviewShows()) {
//                switch (reviewShow.getRating().intValue()) {
//                    case 5:
//                        fiveStarCount++;
//                        break;
//                    case 4:
//                        fourStarCount++;
//                        break;
//                    case 3:
//                        threeStarCount++;
//                        break;
//                    case 2:
//                        twoStarCount++;
//                        break;
//                    case 1:
//                        oneStarCount++;
//                        break;
//                }
//            }
//            bookReview.setFiveStarCount(fiveStarCount);
//            bookReview.setFourStarCount(fourStarCount);
//            bookReview.setThreeStarCount(threeStarCount);
//            bookReview.setTwoStarCount(twoStarCount);
//            bookReview.setOneStarCount(oneStarCount);
//        }
//        return bookReview;
//    }
//
//    public static ApiResponse likeReview(int reviewId, int userId) {
//        try {
//            boolean success = BookReviewDao.likeReview(reviewId, userId);
//            if (success) {
//                return new ApiResponse(true, "Review liked successfully.", null);
//            } else {
//                return new ApiResponse(false, "Failed to like the review.", null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ApiResponse(false, "An error occurred while liking the review.", null);
//        }
//    }
//
//    public static ApiResponse unlikeReview(int reviewId, int userId) {
//
//        try {
//            boolean success = BookReviewDao.unlikeReview(reviewId, userId);
//            if (success) {
//                return new ApiResponse(true, "Review unliked successfully.", null);
//            } else {
//                return new ApiResponse(false, "Failed to unlike the review.", null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ApiResponse(false, "An error occurred while unliking the review.", null);
//        }
//    }
//}
