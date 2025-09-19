package controller;

import com.google.gson.Gson;
import constant.PathConstants;
import model.ApiResponse;
import model.Order;
import model.OrderRequest;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet({
    PathConstants.ORDERS_API,
    PathConstants.ORDER_STATUS_API,
    PathConstants.ORDER_DETAIL_API
})
public class OrderServlet extends HttpServlet {
    private final OrderService orderService;
    private final Gson gson;

    public OrderServlet() throws SQLException {
        this.orderService = new OrderService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        ApiResponse<?> response;

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all orders or filter by status
            String status = req.getParameter("status");
            if (status != null && !status.isEmpty()) {
                response = orderService.getOrdersByStatus(status);
            } else {
                response = orderService.getAllOrders();
            }
        } else {
            // Get order by ID
            try {
                Long orderId = Long.parseLong(pathInfo.substring(1));
                response = orderService.getOrderById(orderId);
            } catch (NumberFormatException e) {
                response = new ApiResponse<>(false, "Invalid order ID format", null);
            }
        }

        sendJsonResponse(resp, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        ApiResponse<?> response;

        if (pathInfo == null || pathInfo.equals("/")) {
            response = new ApiResponse<>(false, "Order ID is required", null);
        } else {
            try {
                // Parse order ID from path
                Long orderId = Long.parseLong(pathInfo.substring(1));

                // Parse request body
                OrderRequest orderRequest = gson.fromJson(req.getReader(), OrderRequest.class);

                if (!orderRequest.isValidStatus()) {
                    response = new ApiResponse<>(false, "Invalid status provided", null);
                } else {
                    response = orderService.updateOrderStatus(orderId, orderRequest.getStatus());
                }
            } catch (NumberFormatException e) {
                response = new ApiResponse<>(false, "Invalid order ID format", null);
            }
        }

        sendJsonResponse(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Forward to order management page for admins
        req.getRequestDispatcher(PathConstants.VIEW_ORDER_MANAGEMENT).forward(req, resp);
    }

    private void sendJsonResponse(HttpServletResponse resp, ApiResponse<?> response) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(response));
    }
}