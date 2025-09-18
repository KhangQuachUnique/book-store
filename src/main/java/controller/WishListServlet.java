package controller;

import constant.PathConstants;
import model.ApiResponse;
import model.WishListRequest;
import util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/wishlist")
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
//        int userId = Integer.parseInt(req.getParameter("userId"));
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        int bookId = body.getBookId();
        ApiResponse response = service.WishListService.addBookToWishList(5, bookId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int userId = Integer.parseInt(req.getParameter("userId"));
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        int bookId = body.getBookId();
        ApiResponse response = service.WishListService.removeBookToWishList(5, bookId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
