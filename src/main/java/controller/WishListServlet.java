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
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        if (pageSizeParam != null) {
            try {
                pageSize = Integer.parseInt(pageSizeParam);
            } catch (NumberFormatException e) {
                pageSize = 5;
            }
        }

        //Get wish list
        WishList wishList = wishListService.getWishListBooks(101L, currentPage, pageSize);
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
        User sessionUser = (User) req.getSession().getAttribute("user");
        
        if (sessionUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }
        
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.addBookToWishList(sessionUser.getId(), bookId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");

        if (sessionUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }
        
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        Long bookId = body.getBookId();
        ApiResponse response = wishListService.removeBookToWishList(sessionUser.getId(), bookId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
