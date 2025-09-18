package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.PathConstants;
import model.ApiResponse;
import model.User;
import model.WishListRequest;
import util.JsonUtil;

@WebServlet("/user/wishlist")
public class WishListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Redirect to login if not authenticated
            resp.sendRedirect(req.getContextPath() + PathConstants.URL_LOGIN);
            return;
        }

        int userId = user.getId().intValue();
        String page = PathConstants.VIEW_WISHLIST;
        req.setAttribute("contentPage", page);
        ApiResponse response = service.WishListService.getWishListBooks(userId);
        if (!response.isSuccess()) {
            req.setAttribute("message", "Your wish list is empty.");
        }
        req.setAttribute("wishListBooks", response.getData());
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }

        int userId = user.getId().intValue();
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        int bookId = body.getBookId();
        ApiResponse response = service.WishListService.addBookToWishList(userId, bookId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }

        int userId = user.getId().intValue();
        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
        int bookId = body.getBookId();
        ApiResponse response = service.WishListService.removeBookToWishList(userId, bookId);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
    }
}
