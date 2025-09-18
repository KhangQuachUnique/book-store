package controller;

import dao.OrderDao;
import dao.OrderStatusDAO;
import model.Order;
import model.OrderStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/order-tracking")
public class OrderTrackingPageServlet extends HttpServlet {
    private OrderStatusDAO orderStatusDAO;

    @Override
    public void init() throws ServletException {
        orderStatusDAO = new OrderStatusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy userId từ query param (sau này thay bằng session login)
        String userIdParam = req.getParameter("userId");
        int userId = 0;
        if (userIdParam != null) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException ignored) {}
        }

        // Lấy statusId từ query param, mặc định = "all"
        String statusId = req.getParameter("statusId");
        if (statusId == null) {
            statusId = "all";
        }

        List<Order> orders = null;
        if (userId > 0) {
            orders = OrderDao.getOrdersByUserIdAndStatus(userId, statusId);
        }

        List<OrderStatus> statuses = orderStatusDAO.getAllStatuses();

        // Gửi dữ liệu sang JSP
        req.setAttribute("orders", orders);
        req.setAttribute("statuses", statuses);
        req.setAttribute("selectedStatus", statusId);
        req.setAttribute("userId", userId); // để JSP build URL filter

        req.getRequestDispatcher("/WEB-INF/views/order-tracking.jsp")
                .forward(req, resp);
    }
}
