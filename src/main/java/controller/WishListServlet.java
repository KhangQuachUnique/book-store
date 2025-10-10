package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;
import model.*;
import service.WishListService;
import util.JsonUtil;

@WebServlet("/user/wishlist")
public class WishListServlet extends HttpServlet {

    private final WishListService wishListService = new WishListService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = PathConstants.VIEW_WISHLIST;
        req.setAttribute("contentPage", page);
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long currentUserId = 0L;

        if (sessionUser != null) {
            currentUserId = sessionUser.getId();
        }
        WishList wishList = wishListService.getWishListBooks(101L, 1, 100);
        if (wishList.getItems().isEmpty()) {
            req.setAttribute("message", "Your wish list is empty.");
        }
        req.setAttribute("wishList", wishList);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long currentUserId = 0L;

        if (sessionUser != null) {
            currentUserId = sessionUser.getId();
        }
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.addBookToWishList(101L, bookId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long currentUserId = 0L;

        if (sessionUser != null) {
            currentUserId = sessionUser.getId();
        }        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.removeBookToWishList(101L, bookId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
