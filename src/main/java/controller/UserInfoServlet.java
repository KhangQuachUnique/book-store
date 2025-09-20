package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import util.JwtUtil;

@WebServlet("/user/profile")
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            String email = JwtUtil.getEmail(token);
            String role = JwtUtil.getRole(token);

            res.addProperty("loggedIn", true);
            res.addProperty("email", email);
            res.addProperty("role", role);
        } else {
            res.addProperty("status", 401);
            res.addProperty("loggedIn", false);
        }
        resp.getWriter().print(res.toString());
    }
}
