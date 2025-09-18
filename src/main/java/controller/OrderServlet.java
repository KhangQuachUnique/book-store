package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constant.PathConstants;
import model.Order;
import service.OrderService;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/orders - Get all orders
                String status = req.getParameter("status");
                List<Order> orders;

                if (status != null && !status.isEmpty()) {
                    orders = OrderService.getOrdersByStatus(status);
                } else {
                    orders = OrderService.getAllOrders();
                }

                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.add("data", gson.toJsonTree(orders));
                resp.getWriter().write(gson.toJson(response));

            } else {
                // GET /api/orders/{id} - Get order by ID
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    try {
                        int orderId = Integer.parseInt(pathParts[1]);
                        Order order = OrderService.getOrderById(orderId);

                        JsonObject response = new JsonObject();
                        if (order != null) {
                            response.addProperty("success", true);
                            response.add("data", gson.toJsonTree(order));
                        } else {
                            response.addProperty("success", false);
                            response.addProperty("message", "Order not found");
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                        resp.getWriter().write(gson.toJson(response));

                    } catch (NumberFormatException e) {
                        JsonObject error = new JsonObject();
                        error.addProperty("success", false);
                        error.addProperty("message", "Invalid order ID");
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write(gson.toJson(error));
                    }
                }
            }

        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("message", "Internal server error: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(error));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("message", "Order ID is required");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(error));
            return;
        }

        try {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length >= 2) {
                int orderId = Integer.parseInt(pathParts[1]);

                // Read request body
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);
                String newStatus = requestData.get("status").getAsString();
                String notes = requestData.has("notes") ?
                        requestData.get("notes").getAsString() : null;

                boolean success;
                if (notes != null && !notes.trim().isEmpty()) {
                    success = OrderService.updateOrderStatusWithNotes(orderId, newStatus, notes);
                } else {
                    success = OrderService.updateOrderStatus(orderId, newStatus);
                }

                JsonObject response = new JsonObject();
                if (success) {
                    response.addProperty("success", true);
                    response.addProperty("message", "Order status updated successfully");

                    // Return updated order data
                    Order updatedOrder = OrderService.getOrderById(orderId);
                    response.add("data", gson.toJsonTree(updatedOrder));
                } else {
                    response.addProperty("success", false);
                    response.addProperty("message", "Failed to update order status");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

                resp.getWriter().write(gson.toJson(response));

            } else {
                JsonObject error = new JsonObject();
                error.addProperty("success", false);
                error.addProperty("message", "Invalid request path");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(error));
            }

        } catch (NumberFormatException e) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("message", "Invalid order ID");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(error));

        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("message", "Internal server error: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(error));
            e.printStackTrace();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle CORS preflight
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
