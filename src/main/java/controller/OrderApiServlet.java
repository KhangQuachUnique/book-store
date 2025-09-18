package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import dao.OrderDao;
import model.Order;

@WebServlet("/api/orders/*") // Sử dụng URL pattern để phân biệt các loại request
public class OrderApiServlet extends HttpServlet {
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Lấy đường dẫn phụ (pathInfo) để xác định loại yêu cầu
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Xử lý API lấy danh sách đơn hàng: /api/orders?userId=1
                String userIdParam = request.getParameter("userId");
                if (userIdParam == null || userIdParam.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Missing userId parameter");
                    return;
                }
                int userId = Integer.parseInt(userIdParam);
                List<Order> orders = OrderDao.getOrdersByUserIdAndStatus(userId, "all");
                out.print(gson.toJson(orders));
            } else if (pathInfo.matches("/\\d+")) { // Xử lý API lấy chi tiết đơn hàng:
                                                    // /api/orders/123
                int orderId = Integer.parseInt(pathInfo.substring(1));
                Order order = OrderDao.getOrderById((long) orderId);
                if (order != null) {
                    out.print(gson.toJson(order));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
        out.flush();
    }
}
