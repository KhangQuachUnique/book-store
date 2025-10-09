//package controller;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import constant.PathConstants;
//import model.ApiResponse;
//import model.User;
//import model.WishListRequest;
//import util.JsonUtil;
//
//@WebServlet("/user/wishlist")
//public class WishListServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String page = PathConstants.VIEW_WISHLIST;
//        req.setAttribute("contentPage", page);
//        User sessionUser = (User) req.getSession().getAttribute("user");
//        Long currentUserId = 0L;
//
//        if (sessionUser != null) {
//            currentUserId = sessionUser.getId();
//        }
//        ApiResponse response = service.WishListService.getWishListBooks(currentUserId.intValue());
//        if (!response.isSuccess()) {
//            req.setAttribute("message", "Your wish list is empty.");
//        }
//        req.setAttribute("wishListBooks", response.getData());
//        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User sessionUser = (User) req.getSession().getAttribute("user");
//        Long currentUserId = 0L;
//
//        if (sessionUser != null) {
//            currentUserId = sessionUser.getId();
//        }
//        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
//        int bookId = body.getBookId();
//        ApiResponse response = service.WishListService.addBookToWishList(currentUserId.intValue(), bookId);
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User sessionUser = (User) req.getSession().getAttribute("user");
//        Long currentUserId = 0L;
//
//        if (sessionUser != null) {
//            currentUserId = sessionUser.getId();
//        }        WishListRequest body = JsonUtil.parseJson(req, WishListRequest.class);
//        int bookId = body.getBookId();
//        ApiResponse response = service.WishListService.removeBookToWishList(currentUserId.intValue(), bookId);
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(new com.google.gson.Gson().toJson(response));
//    }
//}
