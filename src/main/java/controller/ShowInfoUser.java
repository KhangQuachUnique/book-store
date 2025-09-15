package controller;

import constant.PathConstants;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class ShowInfoUser extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        String authHeader = req.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        if (!JwtUtil.validateToken(token)) {
//            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid");
//            return;
//        }
//
//        String email = JwtUtil.getEmailFromToken(token);
        String email = "tthienphuc1612@gmail.com";
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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
