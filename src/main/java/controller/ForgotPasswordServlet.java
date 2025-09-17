package controller;

import com.google.gson.Gson;
import dao.UserDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

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

        // Sinh token reset
//        String token = UUID.randomUUID().toString();
//        userDao.saveResetToken(email, token, Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES)));

        // Gửi email (giả sử có MailService)
//        String resetLink = req.getRequestURL().toString().replace("forgot-password", "reset-password") + "?token=" + token;
//        MailService.send(email, "Password Reset", "Click here to reset your password: " + resetLink);

        resp.getWriter().print(gson.toJson(new Response("Reset link sent to your email")));
    }

    private static class Response {
        String message;
        Response(String msg) { this.message = msg; }
    }
}
