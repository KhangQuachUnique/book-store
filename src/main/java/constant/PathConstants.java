package constant;

public class PathConstants {
    // URL pattern
    public static final String BOOKS_API = "/api/books";
    public static final String USERS_API = "/api/users";

    // Public View paths
    public static final String VIEW_LAYOUT = "/WEB-INF/views/layout.jsp";
    public static final String VIEW_ADMIN_LAYOUT = "/WEB-INF/views/adminLayout.jsp";
    public static final String VIEW_HOME = "/WEB-INF/views/home.jsp";
    public static final String VIEW_PLEASE_LOGIN = "/WEB-INF/views/pleaseLogin.jsp";
    public static final String VIEW_NOT_FOUND = "/WEB-INF/views/notfound.jsp";
    public static final String VIEW_HEADER = "/WEB-INF/views/fragment/header.jsp";
    public static final String VIEW_FOOTER = "/WEB-INF/views/fragment/footer.jsp";
    public static final String VIEW_SIDEBAR = "/WEB-INF/views/fragment/sidebar.jsp";
    public static final String VIEW_USER_INFO = "/WEB-INF/views/viewUserInfo.jsp";
    public static final String EDIT_USER_INFO = "/WEB-INF/views/editUserInfo.jsp";
    public static final String BOOK_DETAIL_PAGE = "/WEB-INF/views/bookDetail.jsp";
    public static final String VIEW_CATEGORY_BOOKS = "/WEB-INF/views/categoryBook.jsp";

    // Private View Path
    public static final String VIEW_WISHLIST = "/WEB-INF/views/wishList.jsp";
    public static final String VIEW_REVIEW = "/WEB-INF/views/review.jsp";
    public static final String VIEW_CART = "/WEB-INF/views/cart.jsp";
    public static final String VIEW_HISTORY = "/WEB-INF/views/viewHistory.jsp";

    // Admin Views
    public static final String VIEW_ADMIN_DASHBOARD = "/WEB-INF/views/userManagement/dashboard.jsp";
    public static final String VIEW_ADMIN_USER_LIST = "/WEB-INF/views/userManagement/userList.jsp";
    public static final String VIEW_ADMIN_USER_EDIT = "/WEB-INF/views/userManagement/editUser.jsp";
    public static final String VIEW_ADMIN_USER_ADD = "/WEB-INF/views/userManagement/createUser.jsp";
    public static final String VIEW_ADMIN_USER_DETAIL = "/WEB-INF/views/userManagement/viewUser.jsp";
    public static final String VIEW_ADMIN_USER_ADD_ADMIN = "/WEB-INF/views/userManagement/creatAdmin.jsp";
    public static final String VIEW_EDIT_USER = "/WEB-INF/views/userManagement/editUser.jsp";

    // Book Management Views
    public static final String VIEW_ADMIN_BOOK = "/WEB-INF/views/userManagement/viewBook.jsp";

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
    public static final String VIEW_ADMIN_USER_ADD_ADDRESS = "/WEB-INF/views/userManagement/newAddress.jsp";
}
