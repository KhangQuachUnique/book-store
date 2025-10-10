//package controller;
//
//import dao.ViewHistoryDao;
//import model.ViewedProductItem;
//import model.User;
//import constant.PathConstants;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet("/user/history")
//public class ViewHistoryServlet extends HttpServlet {
//
//    private final ViewHistoryDao historyDao = new ViewHistoryDao();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        HttpSession session = req.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//
//        // Nếu chưa login -> forward đến trang Please Login
//        if (user == null) {
//            req.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
//            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//            return;
//        }
//
//        // Lấy lịch sử xem của user
//        List<ViewedProductItem> history = historyDao.getHistoryByUserId(user.getId());
//        System.out.println("DEBUG: History size for user " + user.getId() + " = " + history.size());
//        req.setAttribute("history", history);
//
//        // Gắn contentPage và forward sang layout
//        req.setAttribute("contentPage", PathConstants.VIEW_HISTORY);
//        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//    }
//}
