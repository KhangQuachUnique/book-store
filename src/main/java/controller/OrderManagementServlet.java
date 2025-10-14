package controller;

import constant.PathConstants;
import model.Order;
import service.OrderManagementService;
import service.NotificationService;

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
    private final NotificationService notificationService = new NotificationService();
    private static final int ORDERS_PER_PAGE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                String keyword = req.getParameter("keyword");
                String status = req.getParameter("status");
                int page = 1;
                try {
                    String pageParam = req.getParameter("page");
                    if (pageParam != null) {
                        page = Integer.parseInt(pageParam);
                        if (page < 1) page = 1;
                    }
                } catch (NumberFormatException e) {
                    page = 1;
                }

                List<Order> allOrders;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    allOrders = orderService.searchOrders(keyword);
                } else if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
                    allOrders = orderService.getOrdersByStatus(status);
                } else {
                    allOrders = orderService.getAllOrders();
                }

                // Pagination logic
                int totalOrders = allOrders.size();
                int totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);
                if (page > totalPages && totalPages > 0) page = totalPages;

                int startIndex = (page - 1) * ORDERS_PER_PAGE;
                int endIndex = Math.min(startIndex + ORDERS_PER_PAGE, totalOrders);

                List<Order> orders = allOrders.subList(startIndex, endIndex);

                req.setAttribute("orders", orders);
                req.setAttribute("keyword", keyword);
                req.setAttribute("filterStatus", status);
                req.setAttribute("currentPage", page);
                req.setAttribute("totalPages", totalPages);
                req.setAttribute("totalOrders", totalOrders);
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

                // Lấy thông tin đơn hàng trước khi cập nhật
                Order order = orderService.getOrderById(orderId);
                
                boolean updated = orderService.updateOrderStatus(orderId, status);
                if (!updated) {
                    req.getSession().setAttribute("flash_error", "Failed to update order status");
                } else {
                    req.getSession().setAttribute("flash_success", "Order status updated successfully");
                    
                    // Gửi thông báo cho người dùng về việc cập nhật trạng thái đơn hàng
                    if (order != null && order.getUser() != null) {
                        try {
                            String statusMessage = getStatusMessage(status);
                            String title = "Cập nhật trạng thái đơn hàng #" + orderId;
                            String message = "Đơn hàng #" + orderId + " của bạn đã được cập nhật: " + statusMessage;
                            
                            notificationService.createNotification(order.getUser().getId(), title, message);
                        } catch (Exception e) {
                            // Log error nhưng không làm gián đoạn việc cập nhật đơn hàng
                            System.err.println("Error creating notification: " + e.getMessage());
                        }
                    }
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

    /**
     * Chuyển đổi trạng thái đơn hàng thành thông điệp tiếng Việt
     */
    private String getStatusMessage(String status) {
        if (status == null) return "Không xác định";
        
        switch (status.toUpperCase()) {
            case "PROCESSING":
                return "Đang xử lý";
            case "CONFIRMED":
                return "Đã xác nhận";
            case "SHIPPING":
                return "Đang giao hàng";
            case "DELIVERED":
                return "Đã giao hàng";
            case "CANCELLED":
                return "Đã hủy";
            case "RETURNED":
                return "Đã trả hàng";
            default:
                return status;
        }
    }
}
