package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.resend.core.exception.ResendException;

import dao.UserDao;
import model.User;
import util.SendMailUtil;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Display the forgot password form
        req.getRequestDispatcher("/WEB-INF/views/forgotPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();

        try {
            String email = req.getParameter("email");
            System.out.println("DEBUG: Received email parameter: '" + email + "'"); // Debug log

            if (email == null || email.trim().isEmpty()) {
                System.out.println("DEBUG: Email is null or empty"); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Email is required")));
                return;
            }

            email = email.trim();
            System.out.println("DEBUG: Looking up user with email: '" + email + "'"); // Debug log

            Optional<User> userOpt = userDao.findByEmail(email);
            if (!userOpt.isPresent()) {
                System.out.println("DEBUG: User not found for email: '" + email + "'"); // Debug log
                resp.getWriter().print(gson.toJson(new Response("Email not found")));
                return;
            }

            User user = userOpt.get();
            System.out.println("DEBUG: User found: " + user.getName()); // Debug log

            // Generate reset token (reuse verify_token field)
            String token = UUID.randomUUID().toString();
            user.setVerifyToken(token);
            user.setVerifyExpire(Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES)));

            // Update token in database
            userDao.updateVerifyToken(user);
            System.out.println("DEBUG: Token updated in database"); // Debug log

            // Send reset email using existing email utility
            String resetLink = req.getRequestURL().toString().replace("forgot-password", "reset-password") + "?token="
                    + token;
            System.out.println("DEBUG: Sending email to: " + email + " with link: " + resetLink); // Debug log

            SendMailUtil.sendPasswordResetMail(email, resetLink);
            System.out.println("DEBUG: Email sent successfully"); // Debug log

            resp.getWriter().print(gson.toJson(new Response("Reset link sent to your email")));

        } catch (ResendException | IOException e) {
            System.out.println("DEBUG: Email sending failed: " + e.getMessage()); // Debug log
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(new Response("Failed to send reset email")));
        } catch (Exception e) {
            System.out.println("DEBUG: General error: " + e.getMessage()); // Debug log
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(new Response("An error occurred. Please try again.")));
        }
    }

    @SuppressWarnings("unused") // Used by Gson for JSON serialization
    private static class Response {
        String message;

        Response(String msg) {
            this.message = msg;
        }

        public String getMessage() {
            return message;
        }
    }
}
