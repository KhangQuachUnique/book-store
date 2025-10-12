package controller;

import constant.PathConstants;
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

@WebServlet("/admin/orders/*")
public class OrderManagementServlet extends HttpServlet {
    private final OrderManagementService orderService = new OrderManagementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                String keyword = req.getParameter("keyword");
                String status = req.getParameter("status");

                List<Order> orders;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    orders = orderService.searchOrders(keyword);
                } else if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
                    orders = orderService.getOrdersByStatus(status);
                } else {
                    orders = orderService.getAllOrders();
                }

                req.setAttribute("orders", orders);
                req.setAttribute("keyword", keyword);
                req.setAttribute("filterStatus", status);
                req.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
                req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                return;
            }

            // Details fragment for modal: /admin/orders/{id}
            if (pathInfo.matches("/\\d+")) {
                long orderId = Long.parseLong(pathInfo.substring(1));
                Order order = orderService.getOrderDetails(orderId);
                if (order == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    req.setAttribute("error", "Order not found");
                    req.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
                    req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                    return;
                }
                req.setAttribute("order", order);
                // Return an HTML snippet for modal body
                req.getRequestDispatcher("/WEB-INF/views/admin/fragments/order_detail.jsp").forward(req, resp);
                return;
            }

            // Optional: statistics pages if needed
            if ("/stats".equals(pathInfo)) {
                String period = req.getParameter("period");
                Map<String, Object> stats = orderService.getOrderStatistics(period);
                req.setAttribute("stats", stats);
                req.setAttribute("contentPage", "/WEB-INF/views/admin/order-stats.jsp");
                req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                return;
            }

            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
            req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Invalid input: " + e.getMessage());
            req.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
            req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Server error: " + e.getMessage());
            req.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
            req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("updateStatus".equals(action)) {
                long orderId = Long.parseLong(req.getParameter("orderId"));
                String status = req.getParameter("status");

                boolean updated = orderService.updateOrderStatus(orderId, status);
                if (!updated) {
                    req.getSession().setAttribute("flash_error", "Failed to update order status");
                } else {
                    req.getSession().setAttribute("flash_success", "Order status updated successfully");
                }
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            } else if ("delete".equals(action)) {
                long orderId = Long.parseLong(req.getParameter("orderId"));
                boolean deleted = orderService.deleteOrder(orderId);
                if (!deleted) {
                    req.getSession().setAttribute("flash_error", "Order not found or could not be deleted");
                } else {
                    req.getSession().setAttribute("flash_success", "Order deleted successfully");
                }
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            }

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported action");
        } catch (SQLException e) {
            req.getSession().setAttribute("flash_error", "Database error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        } catch (Exception e) {
            req.getSession().setAttribute("flash_error", "Server error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        }
    }
}
