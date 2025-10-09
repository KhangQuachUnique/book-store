package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.Order;
import service.OrderService;
import service.OrderStatusService;

@WebServlet("/user/order-tracking")
public class OrderTrackingPageServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        HttpSession session = req.getSession(false);
//        User user = (session != null) ? (User) session.getAttribute("user") : null;
//
//        if (user == null) {
//            resp.sendRedirect(req.getContextPath() + "/login");
//            return;
//        }
//
//        Long userId = user.getId();
//        String status = req.getParameter("status");
//        if (status == null) {
//            status = "ALL";
//        }

        // ✅ BỎ QUA LOGIN để test
        // Giả lập user id = 1 (hoặc id nào bạn có trong bảng orders)
        model.User mockUser = new model.User();
        mockUser.setId(101L);
        HttpSession session = req.getSession(true);
        session.setAttribute("user", mockUser);

        Long userId = mockUser.getId();
        String status = req.getParameter("status");
        if (status == null) {
            status = "ALL";
        }

        List<Order> orders = orderService.getOrdersByUserAndStatus(userId, status);

        OrderStatusService orderStatusService = new OrderStatusService();
        List<String> statuses = orderStatusService.getAllStatuses();

        req.setAttribute("orders", orders);
        req.setAttribute("statuses", statuses);
        req.setAttribute("selectedStatus", status);
        req.setAttribute("orderStatusService", orderStatusService);

//        req.setAttribute("contentPage", "/WEB-INF/views/order-tracking.jsp");
//        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);

        req.getRequestDispatcher("/WEB-INF/views/order-tracking.jsp").forward(req, resp);
    }
}
