package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.ApiResponse;
import model.Order;
import service.OrderManagementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/orders/*")
public class OrderManagementServlet extends HttpServlet {
    private final OrderManagementService orderService = new OrderManagementService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all orders
                List<Order> orders = orderService.getAllOrders();
                ApiResponse<List<Order>> response = new ApiResponse<List<Order>>(true, "Orders retrieved successfully", orders);
                resp.getWriter().write(gson.toJson(response, new TypeToken<ApiResponse<List<Order>>>(){}.getType()));
            } else if (pathInfo.equals("/stats")) {
                // Get statistics
                String period = req.getParameter("period"); // "daily" or "monthly"
                Map<String, Object> stats = orderService.getOrderStatistics(period);
                ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, "Statistics retrieved successfully", stats);
                resp.getWriter().write(gson.toJson(response, new TypeToken<ApiResponse<Map<String, Object>>>(){}.getType()));
            } else if (pathInfo.equals("/top-books")) {
                // Get top selling books
                String limitStr = req.getParameter("limit");
                int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;
                Map<String, Object> topBooks = orderService.getTopSellingBooks(limit);
                ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, "Top books retrieved successfully", topBooks);
                resp.getWriter().write(gson.toJson(response, new TypeToken<ApiResponse<Map<String, Object>>>(){}.getType()));
            } else if (pathInfo.matches("/\\d+")) {
                // Get order details
                long orderId = Long.parseLong(pathInfo.substring(1));
                Order order = orderService.getOrderDetails(orderId);
                if (order != null) {
                    ApiResponse<Order> response = new ApiResponse<>(true, "Order details retrieved successfully", order);
                    resp.getWriter().write(gson.toJson(response, new TypeToken<ApiResponse<Order>>(){}.getType()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ApiResponse<Object> error = new ApiResponse<>(false, "Order not found", null);
                    resp.getWriter().write(gson.toJson(error));
                }
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> error = new ApiResponse<>(false, "Database error: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<Object> error = new ApiResponse<>(false, "Invalid input: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> error = new ApiResponse<>(false, "Server error: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        try {
            if (pathInfo != null && pathInfo.startsWith("/status/")) {
                // Update order status
                long orderId = Long.parseLong(pathInfo.substring("/status/".length()));
                String status = req.getParameter("status");
                
                if (status == null || status.trim().isEmpty()) {
                    throw new IllegalArgumentException("Status parameter is required");
                }
                
                boolean updated = orderService.updateOrderStatus(orderId, status);
                if (updated) {
                    ApiResponse<Object> response = new ApiResponse<>(true, "Order status updated successfully", null);
                    resp.getWriter().write(gson.toJson(response));
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    ApiResponse<Object> error = new ApiResponse<>(false, "Failed to update order status", null);
                    resp.getWriter().write(gson.toJson(error));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponse<Object> error = new ApiResponse<>(false, "Invalid URL path", null);
                resp.getWriter().write(gson.toJson(error));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> error = new ApiResponse<>(false, "Database error: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<Object> error = new ApiResponse<>(false, "Invalid input: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ApiResponse<Object> error = new ApiResponse<>(false, "Server error: " + e.getMessage(), null);
            resp.getWriter().write(gson.toJson(error));
        }
    }
}