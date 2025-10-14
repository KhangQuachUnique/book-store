package controller;

import service.ReviewService;
import model.ApiResponse;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/review")
public class BookReviewServlet extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long userId = currentUser.getId();
        String bookIdStr = req.getParameter("bookId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        Long bookId = null;
        Integer rating = null;
        try {
            bookId = (bookIdStr != null) ? Long.valueOf(bookIdStr) : null;
            rating = (ratingStr != null) ? Integer.valueOf(ratingStr) : null;
        } catch (NumberFormatException e) {
            rating = null;
        }

        ApiResponse<Void> result = reviewService.addOrUpdateReview(userId, bookId, rating, comment);

        // Redirect back to order tracking; optionally add a message parameter
        String redirectUrl = req.getContextPath() + "/user/order-tracking?status=DELIVERED";
        if (!result.isSuccess()) {
            redirectUrl += "&error=" + java.net.URLEncoder.encode(result.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
        resp.sendRedirect(redirectUrl);
    }
}
