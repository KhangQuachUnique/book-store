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
        resp.setContentType("application/json");
        Gson gson = new Gson();

        try {
            String token = req.getParameter("token");
            String newPassword = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");

            System.out.println("DEBUG Reset: Received token: '" + token + "'"); // Debug log
            System.out.println("DEBUG Reset: Password length: " + (newPassword != null ? newPassword.length() : "null")); // Debug log
            System.out.println("DEBUG Reset: Confirm password length: " + (confirmPassword != null ? confirmPassword.length() : "null")); // Debug log

            // Validate input
            if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
                System.out.println("DEBUG Reset: Invalid request - missing token or password"); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Invalid request", false)));
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("DEBUG Reset: Passwords do not match"); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Passwords do not match", false)));
                return;
            }

            // Validate token and expiry
            User user = userDao.findByVerifyToken(token);
            if (user == null) {
                System.out.println("DEBUG Reset: User not found for token: " + token); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Invalid or expired token", false)));
                return;
            }

            System.out.println("DEBUG Reset: User found: " + user.getName()); // Debug log

            if (user.getVerifyExpire() == null || user.getVerifyExpire().before(Timestamp.from(Instant.now()))) {
                System.out.println("DEBUG Reset: Token expired. Expiry: " + user.getVerifyExpire()); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Token expired", false)));
                return;
            }

            // Update password and clear token
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            userDao.updatePasswordAndClearToken(user.getId(), hashedPassword);
            System.out.println("DEBUG Reset: Password updated successfully for user: " + user.getName()); // Debug log

            resp.getWriter().print(gson.toJson(new Response("Password updated successfully", true)));
            
        } catch (Exception e) {
            System.out.println("DEBUG Reset: Exception occurred: " + e.getMessage()); // Debug log
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(new Response("An error occurred. Please try again.", false)));
        }
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