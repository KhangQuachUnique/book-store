// package controller.dev;

// import com.google.gson.Gson;
// import constant.PathConstants;
// import service.NotificationService;
// import model.Notification;

// import javax.servlet.RequestDispatcher;
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.util.List;
// import java.util.stream.Collectors;

// /**
//  * Temporary dev-only servlet to test notifications without authentication.
//  * WARNING: Remove before production deployment.
//  */
// @WebServlet("/dev/notifications")
// public class DevNotificationServlet extends HttpServlet {

//     private transient NotificationService notificationService;
//     private transient Gson gson;

//     @Override
//     public void init() throws ServletException {
//         notificationService = new NotificationService();
//         gson = new Gson();
//     }

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//         String action = param(req, "action", "list");
//         long userId = parseLongOrDefault(req.getParameter("userId"), 22L);

//         // UI view: forward to existing notifications.jsp through the common layout
//         if ("view".equalsIgnoreCase(action)) {
//             try {
//                 // Optional: mark all as read when requested via query param mark=1|true
//                 String mark = req.getParameter("mark");
//                 if (mark != null && ("1".equals(mark) || Boolean.parseBoolean(mark))) {
//                     notificationService.markAllAsRead(userId);
//                 }

//                 List<Notification> notifications = notificationService.listByUser(userId);
//                 req.setAttribute("notifications", notifications);
//                 // Do NOT mark as read automatically in dev view unless explicitly requested
//                 req.setAttribute("contentPage", "/WEB-INF/views/userManagement/notifications.jsp");
//                 RequestDispatcher dispatcher = req.getRequestDispatcher(PathConstants.VIEW_LAYOUT);
//                 dispatcher.forward(req, resp);
//             } catch (Exception e) {
//                 resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                 prepareJson(resp);
//                 resp.getWriter().write("{\"error\":\"Failed to render notifications view.\"}");
//             }
//             return;
//         }

//         // JSON API paths
//         prepareJson(resp);
//         switch (action) {
//             case "getCount": {
//                 int unread = notificationService.countUnread(userId);
//                 resp.getWriter().write("{" + "\"unreadCount\":" + unread + "}");
//                 break;
//             }
//             case "list":
//             default: {
//                 try {
//                     List<Notification> list = notificationService.listByUser(userId);
//                     // Map to lightweight DTOs to avoid serializing lazy relations
//                     List<NotifDto> dtos = list.stream()
//                             .map(n -> new NotifDto(n.getId(), n.getTitle(), n.getMessage(), n.getIsRead() != null && n.getIsRead()))
//                             .collect(Collectors.toList());
//                     resp.getWriter().write(gson.toJson(dtos));
//                 } catch (Exception e) {
//                     resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                     resp.getWriter().write("{\"error\":\"Failed to fetch notifications. Ensure userId exists.\"}");
//                 }
//                 break;
//             }
//         }
//     }

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//         prepareJson(resp);
//         req.setCharacterEncoding(StandardCharsets.UTF_8.name());

//         String action = param(req, "action", "");
//         long userId = parseLongOrDefault(req.getParameter("userId"), 22L);

//         try {
//             switch (action) {
//                 case "create": {
//                     String title = param(req, "title", "Test Notification");
//                     String message = param(req, "message", "This is a dev notification.");
//                     notificationService.createForUser(userId, title, message);
//                     resp.getWriter().write("{\"ok\":true}");
//                     break;
//                 }
//                 case "markAllRead": {
//                     notificationService.markAllAsRead(userId);
//                     resp.getWriter().write("{\"ok\":true}");
//                     break;
//                 }
//                 default: {
//                     resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                     resp.getWriter().write("{\"error\":\"Unknown action\"}");
//                 }
//             }
//         } catch (Exception e) {
//             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             resp.getWriter().write("{\"error\":\"Operation failed. Ensure userId exists.\"}");
//         }
//     }

//     private static void prepareJson(HttpServletResponse resp) {
//         resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
//         resp.setContentType("application/json; charset=UTF-8");
//     }

//     private static String param(HttpServletRequest req, String name, String def) {
//         String v = req.getParameter(name);
//         return (v == null || v.isEmpty()) ? def : v;
//     }

//     private static long parseLongOrDefault(String s, long def) {
//         try { return Long.parseLong(s); } catch (Exception e) { return def; }
//     }

//     // Lightweight DTO for JSON responses
//     static class NotifDto {
//         Long id; String title; String message; boolean isRead;
//         NotifDto(Long id, String title, String message, boolean isRead) {
//             this.id = id; this.title = title; this.message = message; this.isRead = isRead;
//         }
//     }
// }
