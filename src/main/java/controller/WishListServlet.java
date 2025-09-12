package controller;

import constant.PathConstants;
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
        List<Book> wishListBooks = service.WishListService.getWishListBooks(5);
        if (wishListBooks.isEmpty()) {
            req.setAttribute("message", "Your wish list is empty.");
        }
        req.setAttribute("wishListBooks", wishListBooks);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
