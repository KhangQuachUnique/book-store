package controller;

import com.google.gson.Gson;
import dao.UserDao;
import model.User;
import util.SendMailUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import jakarta.mail.MessagingException;

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
        String email = req.getParameter("email");

        resp.setContentType("application/json");
        Gson gson = new Gson();

        Optional<User> userOpt = userDao.findByEmail(email);
        if (!userOpt.isPresent()) {
            resp.getWriter().print(gson.toJson(new Response("Email not found")));
            return;
        }

        User user = userOpt.get();
        
        // Generate reset token (reuse verify_token field)
        String token = UUID.randomUUID().toString();
        user.setVerifyToken(token);
        user.setVerifyExpire(Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES)));
        
        // Update token in database
        userDao.updateVerifyToken(user);

        // Send reset email using existing email utility
        try {
            String resetLink = req.getRequestURL().toString().replace("forgot-password", "reset-password") + "?token=" + token;
            SendMailUtil.sendPasswordResetMail(email, resetLink);
            resp.getWriter().print(gson.toJson(new Response("Reset link sent to your email")));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(new Response("Failed to send reset email")));
        }
    }

    private static class Response {
        String message;
        Response(String msg) { this.message = msg; }
    }
}
