package controller;

import dao.OrderDAO;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/order-tracking")
public class OrderTrackingPageServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy userId từ query param
        String userIdParam = req.getParameter("userId");
        int userId = 0;

        if (userIdParam != null) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                userId = 0; // nếu sai format thì set mặc định
            }
        }

        // Sau này khi có login thì sẽ lấy từ session thay vì query param
        if (userId > 0) {
            List<Order> orders = orderDAO.getOrdersByUserId(userId);
            req.setAttribute("orders", orders);
        }

        req.getRequestDispatcher("/WEB-INF/views/order-tracking.jsp")
                .forward(req, resp);
    }

}
