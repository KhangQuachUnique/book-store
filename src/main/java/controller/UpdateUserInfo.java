package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import model.User;
import service.UserService;
import util.PasswordUtil;

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
            throws SQLException, IOException {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String action = request.getParameter("action");

        String message = "Không có hành động hợp lệ!";
        String url = "/user/info";
        User user = new User();
        user.setId(sessionUser.getId());
        user.setEmail(sessionUser.getEmail());
        user.setRole(sessionUser.getRole());

        if (action.equals("changeUserInfo")) {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            url = "/user/info";
            if (isChangeInfo(sessionUser, name, phone)) {
                user.setName(name);
                user.setPhone(phone);
                userService.updateUser(user);
                message = "Cập nhật thông tin thành công!";
            }
            else {
                message = "Thông tin không có sự thay đổi!";
            }
        }
        else if (action.equals("changeUserPassword")) {
            String newPassword = request.getParameter("newPassword");
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            if (isChangePassword(sessionUser, newPasswordHash)) {
                user.setPasswordHash(newPasswordHash);
                userService.updateUserPasswordHash(user);
                message = "Cập nhật mật khẩu thành công!";
                url = "/user/info";
            }
            else {
                message = "Mật khẩu mới trùng với mật khẩu hiện tại";
                url = "/user/edit";
            }
        }

        request.getSession().setAttribute("toastMessage", message);
        response.sendRedirect(request.getContextPath() + url);
    }

    private boolean isChangeInfo(User sessionUser, String newName, String newPhone) {
        String name = sessionUser.getName();
        String phone = sessionUser.getPhone();

        if (newName == null) {
            return false;
        }

        return !name.equals(newName) || !phone.equals(newPhone);
    }

    private boolean isChangePassword(User sessionUser, String newPassWordHash) {
        String oldPassWordHash = sessionUser.getPasswordHash();

        return !oldPassWordHash.equals(newPassWordHash);
    }
}
