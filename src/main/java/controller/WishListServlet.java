package controller;

import constant.PathConstants;
import model.ApiResponse;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class WishListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = PathConstants.VIEW_WISHLIST;
        req.setAttribute("contentPage", page);
        ApiResponse response = service.WishListService.getWishListBooks(5);
        if (!response.isSuccess()) {
            req.setAttribute("message", "Your wish list is empty.");
        }
        req.setAttribute("wishListBooks", response.getData());
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int userId = Integer.parseInt(req.getParameter("userId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        ApiResponse response;

        switch (action) {
            case "add":
                response = service.WishListService.addBookToWishList(userId, bookId);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(new com.google.gson.Gson().toJson(response));
                break;

            case "remove":
                response = service.WishListService.removeBookToWishList(userId, bookId);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(new com.google.gson.Gson().toJson(response));
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }
}
