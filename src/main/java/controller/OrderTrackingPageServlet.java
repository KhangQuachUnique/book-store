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

        // ✅ Test mode: giả lập user
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

        req.getRequestDispatcher("/WEB-INF/views/order-tracking.jsp").forward(req, resp);
    }


}