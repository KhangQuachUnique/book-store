package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;

@WebServlet("/admin/orders")
public class OrderManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set content page for order management
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_ORDER_MANAGEMENT);
        request.setAttribute("pageTitle", "Quản lý đơn hàng");

        // Forward to layout
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}
