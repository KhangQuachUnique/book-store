package controller;

import com.google.gson.Gson;
import constant.PathConstants;
import model.Notification;
import model.User;
import service.NotificationService;

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

    private NotificationService notificationService;
    private Gson gson;

    @Override
    public void init() {
        notificationService = new NotificationService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"User not authenticated\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "getCount":
                int unreadCount = notificationService.countUnread(userId);
                Map<String, Integer> countMap = new HashMap<>();
                countMap.put("unreadCount", unreadCount);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(gson.toJson(countMap));
                break;
            case "list":
            default:
                List<Notification> notifications = notificationService.listByUser(userId);
                req.setAttribute("notifications", notifications);
                notificationService.markAllAsRead(userId);
                req.setAttribute("contentPage", "/WEB-INF/views/userManagement/notifications.jsp");
                RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_LAYOUT);
                dispatcher.forward(req, resp);
                break;
        }
    }
}