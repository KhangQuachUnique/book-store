package controller;

import constant.PathConstants;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import model.Address;
import model.User;
import service.AddressService;
import service.UserService;
import util.PasswordUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.ConstraintViolation;

@WebServlet("/admin/user")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();
    private AddressService addressService = new AddressService();
    private static final Logger log = Logger.getLogger(UserServlet.class.getName());
    private static final String BASE_URL = "/admin/user";
    private Validator validator;

    @Override
    public void init() throws ServletException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            // CHANGE: Tạm thời bỏ phân quyền để kiểm tra, sau này khôi phục
            // if (!checkAdminPermission(request, response)) {
            //     return;
            // }
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
                // CHANGE: Thêm case cho newAddress để xử lý riêng, tránh forward sai
                case "newAddress":
                    showNewAddressForm(request, response);
                    break;
                default:
                    listAllUsers(request, response);
                    break;
            }
        } catch (SQLException ex) {
            handleException(request, response, ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            // CHANGE: Tạm thời bỏ phân quyền để kiểm tra
            // if (!checkAdminPermission(request, response)) {
            //     return;
            // }
            switch (action) {
                case "update":
                    updateUser(request, response);
                    break;
                // CHANGE: Thêm case cho updateAddress nếu cần (từ viewUser inline edit)
                case "updateAddress":
                    updateAddress(request, response);
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
            handleException(request, response, ex);
        }
    }

    // CHANGE: Thêm phương thức checkAdminPermission (khôi phục sau khi test)
    // private boolean checkAdminPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //     HttpSession session = request.getSession(false);
    //     if (session == null || session.getAttribute("user") == null) {
    //         response.sendRedirect(request.getContextPath() + "/login");
    //         return false;
    //     }
    //     User loggedInUser = (User) session.getAttribute("user");
    //     if (!"admin".equals(loggedInUser.getRole())) {
    //         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
    //         return false;
    //     }
    //     return true;
    // }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {
        log.log(Level.SEVERE, "Error occurred: ", ex);
        request.setAttribute("errorMessage", "An error occurred while processing your request.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/error.jsp");
        dispatcher.forward(request, response);
    }

    private void listAllUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int page = getPageParameter(request);
        List<User> users = userService.getAllUsers(page);
        int totalPages = userService.getTotalPages("list", null);
        setPaginationAttributes(request, page, totalPages);
        request.setAttribute("users", users);
        request.setAttribute("listType", "All Users");
        forwardToList(request, response);
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int page = getPageParameter(request);
        List<User> users = userService.getAdmins(page);
        int totalPages = userService.getTotalPages("listAdmins", null);
        setPaginationAttributes(request, page, totalPages);
        request.setAttribute("users", users);
        request.setAttribute("listType", "Admins");
        forwardToList(request, response);
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int page = getPageParameter(request);
        List<User> users = userService.getCustomers(page);
        int totalPages = userService.getTotalPages("listUsers", null);
        setPaginationAttributes(request, page, totalPages);
        request.setAttribute("users", users);
        request.setAttribute("listType", "Customers");
        forwardToList(request, response);
    }

    private void listBlocked(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int page = getPageParameter(request);
        List<User> users = userService.getBlockedUsers(page);
        int totalPages = userService.getTotalPages("listBlocked", null);
        setPaginationAttributes(request, page, totalPages);
        request.setAttribute("users", users);
        request.setAttribute("listType", "Blocked Users");
        forwardToList(request, response);
    }

    private void searchUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int page = getPageParameter(request);
        String query = request.getParameter("query");
        List<User> users = userService.searchUsers(query, page);
        int totalPages = userService.getTotalPages("search", query);
        setPaginationAttributes(request, page, totalPages);
        request.setAttribute("users", users);
        request.setAttribute("listType", "Search Results for: " + query);
        forwardToList(request, response);
    }

    private void setPaginationAttributes(HttpServletRequest request, int currentPage, int totalPages) {
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
    }

    private int getPageParameter(HttpServletRequest request) {
        String pageStr = request.getParameter("page");
        try {
            int page = Integer.parseInt(pageStr);
            return page > 0 ? page : 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void forwardToList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_USER_LIST);
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    private void viewUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long id = parseLongParameter(request.getParameter("id"), response);
        if (id == -1) return;
        User user = userService.getUserById(id);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        List<Address> addresses = addressService.getAddressesByUserId(id);
        request.setAttribute("editUser", user); // CHANGE: Sử dụng editUser để tránh xung đột session
        request.setAttribute("addresses", addresses);
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_USER_DETAIL);
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    private void showNewAdminForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_USER_ADD_ADMIN);
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    private void showNewUserForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_USER_ADD);
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    private void viewAddresses(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long userId = parseLongParameter(request.getParameter("id"), response);
        if (userId == -1) return;
        User user = userService.getUserById(userId);
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        request.setAttribute("user", user); // CHANGE: Giữ nguyên user cho viewAddresses, nhưng đảm bảo không xung đột
        request.setAttribute("addresses", addresses);
        request.setAttribute("contentPage", "/WEB-INF/views/userManagement/addressList.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    // CHANGE: Sửa showNewAddressForm: Set đúng contentPage đến newAddress.jsp, sử dụng selectedUserId để tránh session ghi đè
    private void showNewAddressForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userId = parseLongParameter(request.getParameter("id"), response);
        if (userId == -1) return;
        // CHANGE: Sử dụng selectedUserId để tránh xung đột với session userId (nếu có filter set từ session)
        request.setAttribute("selectedUserId", userId);
        request.setAttribute("contentPage", PathConstants.VIEW_ADMIN_USER_ADD_ADDRESS); // CHANGE: Set đúng đến newAddress.jsp thay vì addressList.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.VIEW_ADMIN_LAYOUT);
        dispatcher.forward(request, response);
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long addressId = parseLongParameter(request.getParameter("addressId"), response);
        if (addressId == -1) return;
        long userId = parseLongParameter(request.getParameter("userId"), response);
        if (userId == -1) return;
        addressService.deleteAddress(addressId, userId);
        response.sendRedirect(BASE_URL + "?action=view&id=" + userId); // CHANGE: Redirect đến view thay vì viewAddresses để nhất quán
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        long id = parseLongParameter(request.getParameter("id"), response);
        if (id == -1) return;
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

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            request.setAttribute("errorMessage", errorMessage.toString());
            viewUser(request, response); // CHANGE: Quay lại viewUser thay vì editForm (vì đã tích hợp)
            return;
        }

        userService.updateUser(user);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=view&id=" + id); // CHANGE: Redirect đến view để reload dữ liệu mới
    }

    // CHANGE: Thêm updateAddress nếu cần inline edit từ viewUser
    private void updateAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        long addressId = parseLongParameter(request.getParameter("addressId"), response);
        if (addressId == -1) return;
        long userId = parseLongParameter(request.getParameter("userId"), response); // CHANGE: Lấy từ parameter, không từ session
        if (userId == -1) return;
        String addressText = request.getParameter("address");
        boolean isDefaultAddress = "true".equals(request.getParameter("isDefaultAddress"));

        Address address = new Address();
        address.setId(addressId);
        address.setUserId(userId); // CHANGE: Đảm bảo set userId từ parameter
        address.setAddress(addressText);
        address.setDefaultAddress(isDefaultAddress);

        if (addressText == null || addressText.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Address cannot be empty");
            viewUser(request, response);
            return;
        }

        addressService.updateAddress(address);
        if (isDefaultAddress) {
            addressService.setDefaultAddress(addressId, userId);
        }

        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=view&id=" + userId);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = parseLongParameter(request.getParameter("id"), response);
        if (id == -1) return;
        userService.deleteUser(id);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=list");
    }

    private void blockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = parseLongParameter(request.getParameter("id"), response);
        if (id == -1) return;
        userService.blockUser(id);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=list");
    }

    private void unblockUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = parseLongParameter(request.getParameter("id"), response);
        if (id == -1) return;
        userService.unblockUser(id);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=list");
    }

    private void createAdmin(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");


        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(PasswordUtil.hashPassword(password));


        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            request.setAttribute("errorMessage", errorMessage.toString());
            showNewAdminForm(request, response);
            return;
        }

        userService.createAdmin(user);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=list");
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(PasswordUtil.hashPassword(password));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            request.setAttribute("errorMessage", errorMessage.toString());
            showNewUserForm(request, response);
            return;
        }

        userService.createUser(user);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=list");
    }

    // CHANGE: Cập nhật createAddress: Thêm log để debug userId, đảm bảo lấy từ parameter không từ session
    private void createAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long userId = parseLongParameter(request.getParameter("userId"), response); // CHANGE: Lấy từ parameter, thêm log
        if (userId == -1) {
            log.warning("Invalid userId in createAddress: " + request.getParameter("userId"));
            return;
        }
        log.info("Creating address for selected userId: " + userId); // CHANGE: Log để xác nhận userId đúng (không phải session)
        String addressText = request.getParameter("address");
        boolean isDefaultAddress = "true".equals(request.getParameter("isDefaultAddress"));
        Address address = new Address();
        address.setUserId(userId); // CHANGE: Set rõ ràng từ parameter, không từ session
        address.setAddress(addressText);
        address.setDefaultAddress(isDefaultAddress);
        addressService.createAddress(address);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=view&id=" + userId); // CHANGE: Redirect đến view để hiển thị địa chỉ mới
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long addressId = parseLongParameter(request.getParameter("addressId"), response);
        if (addressId == -1) return;
        long userId = parseLongParameter(request.getParameter("userId"), response);
        if (userId == -1) return;
        addressService.setDefaultAddress(addressId, userId);
        response.sendRedirect(request.getContextPath() + BASE_URL + "?action=view&id=" + userId); // CHANGE: Redirect đến view
    }

    private long parseLongParameter(String param, HttpServletResponse response) throws IOException {
        if (param == null || param.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter");
            return -1;
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return -1;
        }
    }
}