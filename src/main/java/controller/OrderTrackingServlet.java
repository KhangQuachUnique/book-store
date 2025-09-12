package controller;

import model.Order;
import model.OrderItem;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class OrderTrackingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ---- FIX CỨNG DỮ LIỆU GIẢ ----
        List<Order> orders = new ArrayList<>();

        Order order1 = new Order();
        order1.setId(101);
        order1.setCreatedAt("2025-09-12");
        order1.setTotalAmount(250000);
        order1.setStatus("Đang giao");
        order1.setPaymentMethod("COD");

        List<OrderItem> items1 = new ArrayList<>();
        items1.add(new OrderItem("Sách Java Cơ Bản", 1, 150000));
        items1.add(new OrderItem("Thuật toán nâng cao", 1, 100000));
        order1.setItems(items1);

        orders.add(order1);

        // thêm 1 order giả nữa
        Order order2 = new Order();
        order2.setId(102);
        order2.setCreatedAt("2025-09-10");
        order2.setTotalAmount(120000);
        order2.setStatus("Hoàn thành");
        order2.setPaymentMethod("Momo");

        List<OrderItem> items2 = new ArrayList<>();
        items2.add(new OrderItem("Học máy cơ bản", 1, 120000));
        order2.setItems(items2);

        orders.add(order2);

        // ---- TRUYỀN VÀO JSP ----
        request.setAttribute("orders", orders);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/order-tracking.jsp");
        dispatcher.forward(request, response);
    }
}
