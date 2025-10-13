package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import constant.PathConstants;
import model.Order;
import model.User;
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

        //Get user id from section
        User sessionUser = (User) req.getSession().getAttribute("user");
        Long userId = sessionUser.getId();

        //Get filter status from request
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

        req.setAttribute("contentPage", "/WEB-INF/views/order-tracking.jsp");
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }


}