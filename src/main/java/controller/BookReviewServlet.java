package controller;

import constant.PathConstants;
import model.ApiResponse;
import model.BookReview;
import model.BookReviewRequest;
import service.BookReviewService;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/bookReview")
public class BookReviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookIdParam = req.getParameter("bookId");
        if (bookIdParam != null) {
            try {
                int bookId = Integer.parseInt(bookIdParam);
                BookReview bookReview = BookReviewService.getReviewsByBookId(bookId);
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
        BookReviewRequest bookReviewRequest = JsonUtil.parseJson(req, BookReviewRequest.class);
        ApiResponse response = BookReviewService.likeReview(bookReviewRequest.getReviewId());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookReviewRequest bookReviewRequest = JsonUtil.parseJson(req, BookReviewRequest.class);
        ApiResponse response = BookReviewService.unlikeReview(bookReviewRequest.getReviewId());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
