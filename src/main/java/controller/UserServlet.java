package controller;

import model.Address;
import model.User;
import service.AddressService;
import service.UserService;
import util.PasswordUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/user")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();
    private AddressService addressService = new AddressService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "list":
                    listAllUsers(request, response);
                    break;
                case "listAdmins":
                    listAdmins(request, response);
                    break;
                case "listUsers":
                    listCustomers(request, response);
                    break;
                case "listBlocked":
                    listBlocked(request, response);
                    break;
                case "view":
                    viewUser(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "newAdmin":
                    showNewAdminForm(request, response);
                    break;
                case "newUser":
                    showNewUserForm(request, response);
                    break;
                case "search":
                    searchUsers(request, response);
                    break;
                case "viewAddresses":
                    viewAddresses(request, response);
                    break;
                case "newAddress":
                    showNewAddressForm(request, response);
                    break;
                default:
                    listAllUsers(request, response);
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
                case "createAdmin":
                    createAdmin(request, response);
                    break;
                case "createUser":
                    createUser(request, response);
                    break;
                case "search":
                    searchUsers(request, response);
                    break;
                case "createAddress":
                    createAddress(request, response);
                    break;
                case "deleteAddress":
                    deleteAddress(request, response);
                    break;
                case "setDefault":
                    setDefaultAddress(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listAllUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("listType", "All Users");
        forwardToList(request, response);
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> users = userService.getAdmins();
        request.setAttribute("users", users);
        request.setAttribute("listType", "Admins");
        forwardToList(request, response);
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> users = userService.getCustomers();
        request.setAttribute("users", users);
        request.setAttribute("listType", "Customers");
        forwardToList(request, response);
    }

    private void listBlocked(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> users = userService.getBlockedUsers();
        request.setAttribute("users", users);
        request.setAttribute("listType", "Blocked Users");
        forwardToList(request, response);
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String query = request.getParameter("query");
        List<User> users = userService.searchUsers(query);
        request.setAttribute("users", users);
        request.setAttribute("listType", "Search Results for: " + query);
        forwardToList(request, response);
    }

    private void forwardToList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/userList.jsp");
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

    private void showNewAdminForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/createAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/createUser.jsp");
        dispatcher.forward(request, response);
    }

    private void viewAddresses(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long userId = Long.parseLong(request.getParameter("id"));
        User user = userService.getUserById(userId);
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        request.setAttribute("user", user);
        request.setAttribute("addresses", addresses);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/addressList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewAddressForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userId = Long.parseLong(request.getParameter("id"));
        request.setAttribute("userId", userId);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Usermanagement/createAddress.jsp");
        dispatcher.forward(request, response);
    }
    private void deleteAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long addressId = Long.parseLong(request.getParameter("addressId"));
        long userId = Long.parseLong(request.getParameter("userId"));
        addressService.deleteAddress(addressId, userId);
        response.sendRedirect("/admin/user?action=viewAddresses&id=" + userId);
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
        response.sendRedirect("/admin/user?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("/admin/user?action=list");
    }

    private void blockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.blockUser(id);
        response.sendRedirect("/admin/user?action=list");
    }

    private void unblockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.unblockUser(id);
        response.sendRedirect("/admin/user?action=list");
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
        response.sendRedirect("/admin/user?action=list");
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
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
        userService.createUser(user);
        response.sendRedirect("/admin/user?action=list");
    }

    private void createAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long userId = Long.parseLong(request.getParameter("userId"));
        String addressText = request.getParameter("address");
        boolean isDefaultAddress = "true".equals(request.getParameter("isDefaultAddress"));
        Address address = new Address();
        address.setUserId(userId);
        address.setAddress(addressText);
        address.setDefaultAddress(isDefaultAddress);
        addressService.createAddress(address);
        response.sendRedirect("/admin/user?action=viewAddresses&id=" + userId);
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long addressId = Long.parseLong(request.getParameter("addressId"));
        long userId = Long.parseLong(request.getParameter("userId"));
        addressService.setDefaultAddress(addressId, userId);
        response.sendRedirect("/admin/user?action=viewAddresses&id=" + userId);
    }
}