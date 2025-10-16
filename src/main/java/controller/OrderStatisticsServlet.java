package controller;

import constant.PathConstants;
import service.OrderManagementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/orders/statistics/*")
public class OrderStatisticsServlet extends HttpServlet {
    private final OrderManagementService orderService = new OrderManagementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        // Set JSON response
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                // Trả về trang thống kê chính
                req.setAttribute("contentPage", "/WEB-INF/views/admin/order-statistics.jsp");
                req.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT).forward(req, resp);
                return;
            }
            
            // API endpoints
            if ("/daily".equals(pathInfo)) {
                Map<String, Object> dailyStats = orderService.getOrderStatistics("daily");
                resp.getWriter().write(convertToJson(dailyStats));
                return;
            }
            
            if ("/monthly".equals(pathInfo)) {
                Map<String, Object> monthlyStats = orderService.getOrderStatistics("monthly");
                resp.getWriter().write(convertToJson(monthlyStats));
                return;
            }
            
            if ("/top-books".equals(pathInfo)) {
                int limit = 10;
                try {
                    String limitParam = req.getParameter("limit");
                    if (limitParam != null) {
                        limit = Integer.parseInt(limitParam);
                    }
                } catch (NumberFormatException e) {
                    // Sử dụng default limit = 10
                }
                
                Map<String, Object> topBooks = orderService.getTopSellingBooks(limit);
                resp.getWriter().write(convertToJson(topBooks));
                return;
            }
            
            if ("/dashboard".equals(pathInfo)) {
                // Lấy tất cả thống kê cho dashboard
                Map<String, Object> dashboardData = new HashMap<>();
                dashboardData.put("daily", orderService.getOrderStatistics("daily"));
                dashboardData.put("monthly", orderService.getOrderStatistics("monthly"));
                dashboardData.put("topBooks", orderService.getTopSellingBooks(5));
                
                resp.getWriter().write(convertToJson(dashboardData));
                return;
            }
            
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Chuyển đổi Map thành JSON string đơn giản
     */
    private String convertToJson(Map<String, Object> data) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Number) {
                json.append(value);
            } else if (value instanceof java.util.List) {
                json.append(convertListToJson((java.util.List<?>) value));
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mapValue = (Map<String, Object>) value;
                json.append(convertToJson(mapValue));
            } else {
                json.append("\"").append(value.toString()).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Chuyển đổi List thành JSON string
     */
    private String convertListToJson(java.util.List<?> list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        
        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mapItem = (Map<String, Object>) item;
                json.append(convertToJson(mapItem));
            } else if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else {
                json.append(item.toString());
            }
        }
        
        json.append("]");
        return json.toString();
    }
}
