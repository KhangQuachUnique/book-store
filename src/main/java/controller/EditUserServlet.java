package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import constant.PathConstants;
import model.User;
import service.UserService;
import util.PasswordUtil;

@WebServlet("/user/edit")
public class EditUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        User user = (User) request.getSession().getAttribute("user");

        if(action == null) {
            action = "view";
        }

        String page = PathConstants.VIEW_USER_INFO;
        switch (action) {
            case "view":
                page = PathConstants.VIEW_USER_INFO;
                break;
            case "editInfo":
                page = PathConstants.EDIT_USER_INFO;
                break;
            case "changePassword":
                page = PathConstants.EDIT_USER_PASSWORD;
                break;
        }
        request.setAttribute("user", user);
        request.getSession().setAttribute("contentPage", page);
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}
