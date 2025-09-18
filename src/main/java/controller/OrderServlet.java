package controller;

import dao.OrderDao;
import dao.OrderStatusDAO;
import model.Order;
import model.OrderStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderStatusDAO statusDAO;

    @Override
    public void init() throws ServletException {
        statusDAO = new OrderStatusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy userId từ session (giả sử login đã set sẵn userId)
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy statusId từ query param (mặc định = all)
        String statusId = request.getParameter("statusId");
        if (statusId == null || statusId.isEmpty()) {
            statusId = "all";
        }

        // Lấy danh sách trạng thái (thanh menu filter)
        List<OrderStatus> statuses = statusDAO.getAllStatuses();

        // Lấy danh sách đơn hàng theo user + status filter
        List<Order> orders = OrderDao.getOrdersByUserIdAndStatus(userId, statusId);

        request.setAttribute("statuses", statuses);
        request.setAttribute("orders", orders);
        request.setAttribute("selectedStatus", statusId);

        request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
    }
}
