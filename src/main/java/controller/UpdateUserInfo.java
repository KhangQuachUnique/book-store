package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;

@WebServlet("/user/update")
public class UpdateUserInfo extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Không xử lý GET
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            updateInfoUser(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInfoUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        User sessionUser = (User) request.getSession().getAttribute("user");

        String message = "No valid action!";
        String url = "/user/info";

        User user = new User();
        user.setId(sessionUser.getId());
        user.setEmail(sessionUser.getEmail());
        user.setRole(sessionUser.getRole());

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        if (isChangeInfo(sessionUser, name, phone)) {
            sessionUser.setName(name);
            sessionUser.setPhoneNumber(phone);
            userService.updateUser(sessionUser);
            request.getSession().setAttribute("user", sessionUser);

            message = "Information updated successfully!";
        } else {
            message = "Information has not changed!";
        }

        request.getSession().setAttribute("toastMessage", message);
        response.sendRedirect(request.getContextPath() + url);
    }

    private boolean isChangeInfo(User sessionUser, String newName, String newPhone) {
        String name = sessionUser.getName();
        String phone = sessionUser.getPhoneNumber();

        if (newName == null) {
            return false;
        }

        return !name.equals(newName) || !phone.equals(newPhone);
    }

}
