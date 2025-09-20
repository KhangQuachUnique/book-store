package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.PathConstants;
import dao.OrderDAO;
import dao.OrderStatusDAO;
import model.Order;
import model.OrderStatus;
import model.User;
import service.OrderService;

@WebServlet("/user/order-tracking")
public class OrderTrackingPageServlet extends HttpServlet {
    private OrderService orderService;
    private OrderStatusDAO orderStatusDAO;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        orderStatusDAO = new OrderStatusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long userId = user.getId();
        String statusId = req.getParameter("statusId");

        if ("all".equals(statusId)) {
            resp.sendRedirect(req.getContextPath() + "/user/order-tracking");
            return;
        }

        if (statusId == null) {
            statusId = "all";
        }

        // 👉 Gọi Service thay vì gọi DAO trực tiếp
        List<Order> orders = orderService.getOrdersByUserAndStatus(userId, statusId);
        List<OrderStatus> statuses = orderStatusDAO.getAllStatuses();

        req.setAttribute("orders", orders);
        req.setAttribute("statuses", statuses);
        req.setAttribute("selectedStatus", statusId);

        req.setAttribute("contentPage", "/WEB-INF/views/order-tracking.jsp");
        req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
    }
}

