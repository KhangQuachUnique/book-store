package controller;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import dao.UserDao;
import model.User;
import service.UserService;

public class UpdateUserServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String email = request.getParameter("email");

        if (idStr == null || email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            long id = Long.parseLong(idStr);
            User user = userService.getUserById(id);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // Nếu cần update thêm dữ liệu khác thì lấy ở đây
            // ví dụ: name, phone, address...
            // user.setName(request.getParameter("name"));

            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/editUserInfo.jsp").forward(request, response);
        }
        catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user id");
        }
        catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
