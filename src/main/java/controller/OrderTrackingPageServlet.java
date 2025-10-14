package controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.Order;
import model.OrderItem;
import model.User;
import service.OrderService;
import service.OrderStatusService;
import service.ReviewService;

@WebServlet("/user/order-tracking")
public class OrderTrackingPageServlet extends HttpServlet {
    private OrderService orderService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //Lấy user thật từ session (được set khi login hoặc AuthFilter)
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        //Nếu chưa đăng nhập thì chuyển về trang login
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        //Lấy userId từ user thật
        Long userId = currentUser.getId();

        //Lấy trạng thái filter (nếu có)
        String status = req.getParameter("status");
        if (status == null) {
            status = "ALL";
        }

        //Lấy danh sách đơn hàng theo userId + trạng thái
        List<Order> orders = orderService.getOrdersByUserAndStatus(userId, status);

        // Build list of book IDs from orders for quick check which were reviewed
        Set<Integer> bookIdSet = new HashSet<>();
        for (Order o : orders) {
            if (o.getItems() == null) continue;
            for (OrderItem item : o.getItems()) {
                if (item.getBook() != null && item.getBook().getId() != null) {
                    bookIdSet.add(item.getBook().getId());
                }
            }
        }
        List<Integer> bookIds = new ArrayList<>(bookIdSet);
        Set<Integer> reviewedIds = reviewService.getReviewedBookIds(userId, bookIds);
        Map<Integer, Boolean> reviewedMap = new HashMap<>();
        for (Integer bid : reviewedIds) reviewedMap.put(bid, true);

        //Lấy danh sách trạng thái để hiển thị filter
        OrderStatusService orderStatusService = new OrderStatusService();
        List<String> statuses = orderStatusService.getAllStatuses();

        //Gửi dữ liệu sang JSP
        req.setAttribute("orders", orders);
        req.setAttribute("statuses", statuses);
        req.setAttribute("selectedStatus", status);
        req.setAttribute("orderStatusService", orderStatusService);
        req.setAttribute("reviewedMap", reviewedMap);

        //Chuyển hướng sang trang JSP
        req.setAttribute("contentPage", "/WEB-INF/views/order-tracking.jsp");
        req.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(req, resp);
    }
}
