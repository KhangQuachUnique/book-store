package controller;

import com.google.gson.JsonObject;
import constant.PathConstants;
import model.User;
import service.UserService;
import util.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/info")
public class ShowInfoUser extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String token = null;
        if (req.getCookies() != null) {
            for (var c : req.getCookies()) {
                if ("access_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        JsonObject res = new JsonObject();

        if (token != null && JwtUtil.validateToken(token)) {
            String email = JwtUtil.getEmailFromToken(token);
            User user;
            try {
                user = userService.getUserByEmail(email);
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }

            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            String page = PathConstants.VIEW_USER_INFO;
            req.getSession().setAttribute("contentPage", page);
            req.getSession().setAttribute("user", user);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        } else {
                res.addProperty("status", 401);
                res.addProperty("loggedIn", false);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
