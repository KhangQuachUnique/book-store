package controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        boolean verified = false;

        if (token != null && !token.isEmpty())
            verified = userService.verifyUser(token);

        // Redirect đến login kèm thông tin verified
        resp.sendRedirect(req.getContextPath() + "/login?verified=" + verified);
    }
}
