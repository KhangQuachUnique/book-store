package controller;

import constant.PathConstants;
import com.google.gson.Gson;
import dao.NotificationDao;
import model.Notification;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user/notifications")
public class NotificationServlet extends HttpServlet {

    private NotificationDao notificationDao;
    private Gson gson;

    @Override
    public void init() {
        notificationDao = new NotificationDao();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        // --- BẮT BUỘC: Phải kiểm tra người dùng đã đăng nhập chưa ---
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"User not authenticated\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId(); // Giả sử model User có getId()

        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định là lấy danh sách
        }

        switch (action) {
            case "getCount":
                // Action này dùng cho JavaScript gọi để cập nhật số trên chuông
                int unreadCount = notificationDao.countUnreadByUserId(userId);
                Map<String, Integer> countMap = new HashMap<>();
                countMap.put("unreadCount", unreadCount);

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(gson.toJson(countMap));
                break;

            case "list":
            default:
                // Lấy tất cả thông báo để hiển thị trên trang notifications.jsp
                List<Notification> notifications = notificationDao.findByUserId(userId);
                req.setAttribute("notifications", notifications);

                // Sau khi user vào trang này, đánh dấu tất cả là đã đọc
                notificationDao.markAllAsRead(userId);

                // Forward to the correct JSP path (folder name is case-sensitive)
                req.setAttribute("contentPage", "/WEB-INF/views/userManagement/notifications.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_LAYOUT);
                dispatcher.forward(req, resp);
                break;
        }
    }
}