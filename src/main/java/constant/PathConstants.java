package constant;

public class PathConstants {
    // Public View paths
    public static final String VIEW_LAYOUT = "/WEB-INF/views/layout.jsp";
    public static final String VIEW_ADMIN_LAYOUT = "/WEB-INF/views/adminLayout.jsp";
    public static final String VIEW_HOME = "/WEB-INF/views/home.jsp";
    public static final String VIEW_PLEASE_LOGIN = "/WEB-INF/views/pleaseLogin.jsp";
    public static final String VIEW_NOT_FOUND = "/WEB-INF/views/notfound.jsp";
    public static final String VIEW_ERROR = "/WEB-INF/views/error.jsp";

    // Fragment paths
    public static final String VIEW_HEADER = "/WEB-INF/views/fragment/header.jsp";
    public static final String VIEW_FOOTER = "/WEB-INF/views/fragment/footer.jsp";
    public static final String VIEW_SIDEBAR = "/WEB-INF/views/fragment/sidebar.jsp";

    // Authentication Views
    public static final String VIEW_LOGIN = "/WEB-INF/views/login.jsp";
    public static final String VIEW_REGISTER = "/WEB-INF/views/register.jsp";
    public static final String VIEW_FORGOT_PASSWORD = "/WEB-INF/views/forgotPassword.jsp";
    public static final String VIEW_RESET_PASSWORD = "/WEB-INF/views/resetPassword.jsp";

    // User Views
    public static final String VIEW_USER_INFO = "/WEB-INF/views/viewUserInfo.jsp";
    public static final String EDIT_USER_INFO = "/WEB-INF/views/editUserInfo.jsp";

    // Private View Path
    public static final String VIEW_WISHLIST = "/WEB-INF/views/wishList.jsp";
    public static final String VIEW_REVIEW = "/WEB-INF/views/review.jsp";
    public static final String VIEW_CART = "/WEB-INF/views/cart.jsp";
    public static final String VIEW_ORDERS = "/WEB-INF/views/orders.jsp";
    public static final String VIEW_ORDER_TRACKING = "/WEB-INF/views/order-tracking.jsp";

    // Book Views
    public static final String BOOK_DETAIL_PAGE = "/WEB-INF/views/bookDetail.jsp";
    public static final String VIEW_CATEGORY_BOOKS = "/WEB-INF/views/categoryBook.jsp";

    // Category Management Views
    public static final String VIEW_CATEGORY_ADD = "/WEB-INF/views/CategoryManagement/addCategory.jsp";
    public static final String VIEW_CATEGORY_EDIT = "/WEB-INF/views/CategoryManagement/editCategory.jsp";
    public static final String VIEW_CATEGORY_MANAGE = "/WEB-INF/views/CategoryManagement/manageCategory.jsp";

    // Admin Views
    public static final String VIEW_ADMIN_DASHBOARD = "/WEB-INF/views/userManagement/dashboard.jsp";
    public static final String VIEW_ADMIN_USER_LIST = "/WEB-INF/views/userManagement/userList.jsp";
    public static final String VIEW_ADMIN_USER_EDIT = "/WEB-INF/views/userManagement/editUser.jsp";
    public static final String VIEW_ADMIN_USER_ADD = "/WEB-INF/views/userManagement/createUser.jsp";
    public static final String VIEW_ADMIN_USER_DETAIL = "/WEB-INF/views/userManagement/viewUser.jsp";
    public static final String VIEW_ADMIN_USER_ADD_ADMIN = "/WEB-INF/views/userManagement/creatAdmin.jsp";
    public static final String VIEW_ADMIN_NOTIFICATIONS = "/WEB-INF/views/userManagement/notifications.jsp";
    public static final String VIEW_ADMIN_ORDER_MANAGEMENT = "/WEB-INF/views/admin/order-management.jsp";
    public static final String VIEW_ADMIN_ADDRESS_LIST = "/WEB-INF/views/userManagement/addressList.jsp";
    public static final String VIEW_EDIT_USER = "/WEB-INF/views/userManagement/editUser.jsp";

    // URL Paths (for redirects and mappings)
    public static final String URL_HOME = "/home";
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_REGISTER = "/register";
    public static final String URL_BOOK_DETAIL = "/book-detail";
    public static final String URL_CATEGORY_BOOKS = "/category-books";
    public static final String URL_CART = "/user/cart";
    public static final String URL_WISHLIST = "/user/wishlist";
    public static final String URL_ORDERS = "/user/orders";
    public static final String URL_USER_INFO = "/user/info";
}
