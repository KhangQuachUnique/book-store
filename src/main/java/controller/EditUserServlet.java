package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;

@WebServlet("/user/edit")
public class EditUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "view";
        }

        String page = PathConstants.VIEW_USER_INFO;
        switch (action) {
            case "view":
                page = PathConstants.VIEW_USER_INFO;
                break;
            case "edit":
                page = PathConstants.EDIT_USER_INFO;
                break;
        }

        request.getSession().setAttribute("contentPage", page);
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}
