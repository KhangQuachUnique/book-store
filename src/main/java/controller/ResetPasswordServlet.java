package controller;

import com.google.gson.Gson;
import dao.UserDao;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        
        if (token == null || token.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login?error=invalid_token");
            return;
        }
        
        // Check if token is valid and not expired
        User user = userDao.findByVerifyToken(token);
        if (user == null || user.getVerifyExpire() == null || 
            user.getVerifyExpire().before(Timestamp.from(Instant.now()))) {
            resp.sendRedirect(req.getContextPath() + "/login?error=expired_token");
            return;
        }
        
        // Forward to reset password form with token
        req.setAttribute("token", token);
        req.getRequestDispatcher("/WEB-INF/views/resetPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String newPassword = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        resp.setContentType("application/json");
        Gson gson = new Gson();

        // Validate input
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            resp.getWriter().print(gson.toJson(new Response("Invalid request", false)));
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            resp.getWriter().print(gson.toJson(new Response("Passwords do not match", false)));
            return;
        }

        // Validate token and expiry
        User user = userDao.findByVerifyToken(token);
        if (user == null) {
            resp.getWriter().print(gson.toJson(new Response("Invalid or expired token", false)));
            return;
        }

        if (user.getVerifyExpire() == null || user.getVerifyExpire().before(Timestamp.from(Instant.now()))) {
            resp.getWriter().print(gson.toJson(new Response("Token expired", false)));
            return;
        }

        // Update password and clear token (reuse existing method pattern)
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userDao.updatePasswordAndClearToken(user.getId(), hashedPassword);

        resp.getWriter().print(gson.toJson(new Response("Password updated successfully", true)));
    }

    private static class Response {
        String message;
        boolean success;
        
        Response(String message, boolean success) {
            this.message = message;
            this.success = success;
        }
    }
}