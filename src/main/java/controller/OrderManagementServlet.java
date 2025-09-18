package controller;

import constant.PathConstants;
import service.OrderService;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/orders")
public class OrderManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set content page for order management
        request.setAttribute("contentPage", "/WEB-INF/views/admin/order-management.jsp");
        request.setAttribute("pageTitle", "Quản lý đơn hàng");

        // Forward to layout
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}
