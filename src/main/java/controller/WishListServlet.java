package controller;

import java.io.IOException;

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
        //Pagination parameters
        int currentPage = 1;      // default
        int pageSize = 5;  // default

        String pageParam = req.getParameter("page");
        String pageSizeParam = req.getParameter("pageSize");

        if (pageParam != null) {
            currentPage = Integer.parseInt(pageParam);
        }

        if (pageSizeParam != null) {
            pageSize = Integer.parseInt(pageSizeParam);
        }

        //Get user id from section
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long userId = sessionUser.getId();

        //Get wish list
        WishList wishList = wishListService.getWishListBooksByPage(userId, currentPage, pageSize);
        if (wishList.getItems().isEmpty()) {
            req.setAttribute("message", "Your wish list is empty.");
        }
        req.setAttribute("wishList", wishList);
        req.setAttribute("currentPage", wishList.getCurrentPage());
        req.setAttribute("pageSize", wishList.getPageSize());
        req.setAttribute("totalPages", wishList.getTotalPages());

        req.setAttribute("contentPage", PathConstants.VIEW_WISHLIST);
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get user id from section
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long userId = sessionUser.getId();

        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.addBookToWishList(userId, bookId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get user id from section
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long userId = sessionUser.getId();

        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.removeBookToWishList(userId, bookId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
