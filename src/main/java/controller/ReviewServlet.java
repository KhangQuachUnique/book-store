package controller;

import constant.PathConstants;
import model.Review;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.List;

public class ReviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookIdParam = req.getParameter("bookId");
        if (bookIdParam != null) {
            try {
                int bookId = Integer.parseInt(bookIdParam);
                List<Review> reviews = service.ReviewService.getReviewsByBookId(bookId);
                req.setAttribute("reviews", reviews);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("contentPage", PathConstants.VIEW_REVIEW);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }
}
