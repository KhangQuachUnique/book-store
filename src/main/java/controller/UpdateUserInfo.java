package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import model.User;
import service.UserService;
import util.PasswordUtil;

public class UpdateUserInfo extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            updateInfoUser(request,response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInfoUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        User sessionUser = (User) request.getSession().getAttribute("user");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");

        User user = new User();
        user.setId(sessionUser.getId());

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }
        else {
            user.setName(sessionUser.getName());
        }

        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email);
        }
        else {
            user.setEmail(sessionUser.getEmail());
        }

        if (phone != null && !phone.trim().isEmpty()) {
            user.setPhone(phone);
    }
        else {
            user.setPhone(sessionUser.getPhone());
        }

        if (newPassword != null) {
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(newPasswordHash);
        }
        if (isChange(sessionUser, name,  email, phone)) {
            userService.updateUser(user);
        }

        response.sendRedirect("/user/info");
    }

    private boolean isChange(User sessionUser, String newName, String newEmail, String newPhone) {
        String name = sessionUser.getName();
        String email = sessionUser.getEmail();
        String phone = sessionUser.getPhone();
        if (!newName.equals(name) || !newEmail.equals(email) || !newPhone.equals(phone)) {
            return true;
        }
        return false;
    }
}
