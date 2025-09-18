package controller;

import constant.PathConstants;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * HomeServlet handles all home page requests and root path redirections.
 * Serves as the main landing page controller for the BookieCake application.
 * 
 * @author BookieCake Team
 * @version 1.0
 */
@WebServlet(urlPatterns = {"/home", "/", ""})
public class HomeServlet extends HttpServlet {

    /**
     * Handles GET requests for home page display.
     * Sets up the home page content and forwards to the layout template.
     * 
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the content page for home
        request.setAttribute("contentPage", PathConstants.VIEW_HOME);
        
        // Forward to the main layout template
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }

    /**
     * Handles POST requests by delegating to doGet method.
     * This ensures consistent behavior for both GET and POST requests to the home page.
     * 
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

