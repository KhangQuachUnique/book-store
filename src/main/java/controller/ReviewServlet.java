package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;
import model.ApiResponse;
import model.BookReview;
import model.ReviewRequest;
import model.User;
import service.ReviewService;
import util.JsonUtil;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookIdParam = "330658";
        if (bookIdParam != null) {
            try {
                Long bookId = Long.parseLong(bookIdParam);
                User sessionUser = (User) req.getSession().getAttribute("user");
                Long currentUserId = 0L;

                if (sessionUser != null) {
                    currentUserId = sessionUser.getId();
                }

                BookReview bookReview = reviewService.getReviewsByBookId(330658L, 101L);
                req.setAttribute("bookReview", bookReview);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("contentPage", PathConstants.VIEW_REVIEW);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReviewRequest reviewRequest = JsonUtil.parseJson(req, ReviewRequest.class);
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long currentUserId = 0L;

        if (sessionUser != null) {
            currentUserId = sessionUser.getId();
        }
        ApiResponse response = reviewService.likeReview(reviewRequest.getReviewId(), 101L);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        BookReviewRequest bookReviewRequest = JsonUtil.parseJson(req, BookReviewRequest.class);
//        User sessionUser = (User) req.getSession().getAttribute("user");
//        Long currentUserId = 0L;
//
//        if (sessionUser != null) {
//            currentUserId = sessionUser.getId();
//        }
//        ApiResponse response = BookReviewService.unlikeReview(bookReviewRequest.getReviewId(), currentUserId.intValue());
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
//    }
}
