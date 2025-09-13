package controller;

import com.google.gson.JsonObject;
import util.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/me")
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.validateToken(token)) {
                String email = JwtUtil.getEmailFromToken(token);

                JsonObject res = new JsonObject();
                res.addProperty("loggedIn", true);
                res.addProperty("email", email);

                resp.getWriter().print(res.toString());
                return;
            }
        }

        JsonObject res = new JsonObject();
        res.addProperty("loggedIn", false);
        resp.getWriter().print(res.toString());
    }
}
