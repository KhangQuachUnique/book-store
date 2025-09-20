//package controller;
//
//import com.google.gson.Gson;
//import model.Order;
//import model.User;
//import service.OrderService;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet("/api/user/orders")
//public class OrderApiServlet extends HttpServlet {
//    private OrderService orderService;
//    private Gson gson;
//
//    @Override
//    public void init() throws ServletException {
//        orderService = new OrderService();
//        gson = new Gson();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        resp.setContentType("application/json;charset=UTF-8");
//        HttpSession session = req.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//
//        if (user == null) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.getWriter().print(gson.toJson(new ApiError("User not logged in")));
//            return;
//        }
//
//        Long userId = user.getId();
//        String statusId = req.getParameter("statusId"); // may be "all" or null or numeric string
//
//        List<Order> orders = orderService.getOrdersByUserAndStatus(userId, statusId);
//
//        resp.getWriter().print(gson.toJson(orders));
//    }
//
//    // Simple error DTO
//    static class ApiError {
//        private final String error;
//        ApiError(String error) { this.error = error; }
//        public String getError() { return error; }
//    }
//}
