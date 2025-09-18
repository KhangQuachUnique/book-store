package controller;

import constant.PathConstants;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("contentPage", PathConstants.VIEW_HOME);
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}

