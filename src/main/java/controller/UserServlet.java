package controller;

import model.User;
import service.UserService;
import util.PasswordUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "list":
                    listUsers(request, response);
                    break;
                case "view":
                    viewUser(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "block":
                    blockUser(request, response);
                    break;
                case "unblock":
                    unblockUser(request, response);
                    break;
                case "create":
                    createAdmin(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/users.jsp");
        dispatcher.forward(request, response);
    }


    private void viewUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        User user = userService.getUserById(id);
        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/viewUser.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        User user = userService.getUserById(id);
        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/editUser.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/createAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        userService.updateUser(user);
        response.sendRedirect("/api/user?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("/api/user?action=list");
    }

    private void blockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.blockUser(id);
        response.sendRedirect("/api/user?action=list");
    }
    private void unblockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.unblockUser(id);
        response.sendRedirect("/api/user?action=list");
    }

    private void createAdmin(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setPhone(phone);
        userService.createAdmin(user);
        response.sendRedirect("/api/user?action=list");
    }
}