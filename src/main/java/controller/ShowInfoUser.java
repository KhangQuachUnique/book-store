//package controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import constant.PathConstants;
//import model.Address;
//import model.User;
//
//@WebServlet("/user/info")
//public class ShowInfoUser extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        User user = (User) req.getSession().getAttribute("user");
//
//        if (user == null) {
//            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
//            return;
//        }
//
//        String defaultAddress = null;
//
//        List<Address> addressList = user.getAddresses();
//
//        if (addressList != null) {
//            for (Address addr : addressList) {
//                if (addr.isDefaultAddress()) {
//                    defaultAddress = addr.getAddress();
//                }
//            }
//        }
//
//        if (defaultAddress == null) {
//            defaultAddress = "None";
//        }
//
//        req.getSession().setAttribute("defaultAddress", defaultAddress);
//
//        String page = PathConstants.VIEW_USER_INFO;
//        req.setAttribute("contentPage", page);
//        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        doPost(req, resp);
//    }
//}
