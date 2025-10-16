package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.PathConstants;
import model.Address;
import model.Cart;
import model.CartItem;
import model.User;
import service.AddressService;
import service.CartService;

/**
 * Servlet xử lý giỏ hàng
 * URL: /user/cart
 * Chức năng:
 * - GET: Hiển thị giỏ hàng của người dùng
 * - POST: Thêm/Xóa/Cập nhật sản phẩm trong giỏ hàng
 */
@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CartServlet.class.getName());
    // Phí ship cố định: 30,000 VNĐ
    private static final long SHIPPING_FEE = 30_000L;

    private final CartService cartService = new CartService();
    private final AddressService addressService = new AddressService();

    /**
     * Xử lý GET request - Hiển thị trang giỏ hàng
     * Các bước:
     * 1. Lấy thông tin user từ session
     * 2. Load giỏ hàng từ database
     * 3. Tính tổng tiền (chưa bao gồm ship)
     * 4. Lấy danh sách địa chỉ giao hàng
     * 5. Truyền dữ liệu sang JSP để hiển thị
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy action từ request
        String action = req.getParameter("action");

        // XỬ LÝ ACTION COUNT - Trả về số lượng items trong giỏ hàng (cho AJAX)
        if ("count".equals(action)) {
            User user = (User) req.getSession().getAttribute("user");

            // Nếu chưa đăng nhập, trả về 401
            if (user == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"success\":false,\"message\":\"Not logged in\"}");
                return;
            }

            try {
                // Lấy số lượng items từ service
                int count = cartService.getItemCount(user.getId());

                // Trả về JSON response
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"success\":true,\"count\":" + count + "}");
                return;
            } catch (Exception e) {
                log.log(Level.SEVERE, "Failed to count cart items", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
                return;
            }
        }

        // Lấy user hiện tại từ session
        User user = (User) req.getSession().getAttribute("user");

        try {
            // Set trang view là cart.jsp
            req.setAttribute("contentPage", PathConstants.VIEW_CART);

            // Kiểm tra xem có lỗi từ request trước không (từ redirect)
            if ("true".equals(req.getParameter("error"))) {
                req.setAttribute("error", "An error occurred while updating your cart.");
            }

            // Lấy giỏ hàng của user từ database
            Cart cart = cartService.getCart(user.getId());
            // Nếu giỏ hàng null hoặc rỗng thì trả về list rỗng
            List<CartItem> items = cart != null && cart.getItems() != null ? cart.getItems() : Collections.emptyList();

            // Tính tổng tiền các sản phẩm (subtotal - chưa bao gồm ship)
            double cartTotal = cartService.calculateSubtotal(items);
            // Lấy danh sách địa chỉ giao hàng của user
            List<Address> addresses = addressService.getAddressesByUserId(user.getId());

            // Truyền dữ liệu sang JSP
            req.setAttribute("cartTotal", cartTotal);        // Tổng tiền sản phẩm
            req.setAttribute("shippingFee", SHIPPING_FEE);   // Phí ship
            req.setAttribute("cart", items);                 // Danh sách sản phẩm
            req.setAttribute("addresses", addresses);        // Địa chỉ giao hàng

            // Forward sang layout.jsp để render
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        } catch (Exception e) {
            // Xử lý lỗi khi load giỏ hàng
            log.log(Level.SEVERE, "Failed to load cart", e);
            req.setAttribute("contentPage", PathConstants.VIEW_CART);
            req.setAttribute("error", "An error occurred while loading your cart.");
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        }
    }

    /**
     * Xử lý POST request - Thao tác với giỏ hàng
     * Hỗ trợ 3 actions:
     * 1. "add" - Thêm sản phẩm vào giỏ
     * 2. "remove" - Xóa sản phẩm khỏi giỏ
     * 3. "update" - Cập nhật số lượng sản phẩm (AJAX)
     *
     * Parameters:
     * - action: loại thao tác (add/remove/update)
     * - bookId: ID sách (dùng cho add)
     * - cartId: ID cart item (dùng cho remove/update)
     * - quantity: số lượng (dùng cho add/update)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Lấy action từ request (add/remove/update)
        String action = req.getParameter("action");

        // Lấy thông tin user hiện tại
        User user = (User) req.getSession().getAttribute("user");
        Long userId = user.getId();

        try {
            // ACTION 1: THÊM SẢN PHẨM VÀO GIỎ HÀNG
            if ("add".equals(action)) {
                // Lấy ID sách cần thêm
                Long bookId = Long.parseLong(req.getParameter("bookId"));
                // Mặc định số lượng là 1, trừ khi user chỉ định
                int quantity = 1;
                if (req.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(req.getParameter("quantity"));
                }
                // Gọi service để thêm vào database
                cartService.addToCart(userId, bookId, quantity);

                // Kiểm tra xem có phải AJAX request không
                // (Nếu là AJAX thì trả về JSON thay vì redirect)
                String requestedWith = req.getHeader("X-Requested-With");
                String ajax = req.getParameter("ajax");
                if ("XMLHttpRequest".equals(requestedWith) || "true".equals(ajax)) {
                    // Trả về JSON response cho AJAX request
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"success\":true,\"message\":\"Book added to cart successfully!\"}");
                    return;
                }
            }
            // ACTION 2: XÓA SẢN PHẨM KHỎI GIỎ HÀNG
            else if ("remove".equals(action)) {
                // Lấy ID của cart item cần xóa
                Long cartId = Long.parseLong(req.getParameter("cartId"));
                // Gọi service để xóa khỏi database
                cartService.removeItem(userId, cartId);
            }
            // ACTION 3: CẬP NHẬT SỐ LƯỢNG (REAL-TIME AJAX)
            else if ("update".equals(action)) {
                // Lấy ID cart item và số lượng mới
                Long cartId = Long.parseLong(req.getParameter("cartId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));

                // Cập nhật số lượng và nhận kết quả tính toán
                CartService.CartUpdateResult result = cartService.updateItemQuantity(userId, cartId, quantity);

                // Trả về JSON với:
                // - itemTotal: tổng tiền của sản phẩm này (price × quantity)
                // - cartTotal: tổng tiền toàn bộ giỏ hàng
                resp.setContentType("application/json");
                resp.getWriter().write(String.format(Locale.US,
                        "{\"success\":true,\"itemTotal\":%.0f,\"cartTotal\":%.0f}",
                        result.itemTotal(),   // Tiền của item này
                        result.cartTotal()));  // Tổng tiền toàn giỏ
                return; // Kết thúc, không redirect
            }

            // Redirect về trang giỏ hàng (trừ action "update" vì đã return JSON)
            if (!"update".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/user/cart");
            }

        } catch (Exception e) {
            // Xử lý lỗi
            log.log(Level.SEVERE, "Cart action failed", e);

            // Kiểm tra xem có phải AJAX request không để trả về response phù hợp
            String requestedWith = req.getHeader("X-Requested-With");
            String ajax = req.getParameter("ajax");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith) || "true".equals(ajax);

            if ("update".equals(action) || isAjax) {
                // Trả về JSON error cho AJAX
                resp.setContentType("application/json");
                resp.setStatus(500);
                resp.getWriter().write("{\"success\":false,\"message\":\"An error occurred while processing your request\"}");
            } else {
                // Redirect với error flag cho non-AJAX request
                resp.sendRedirect(req.getContextPath() + "/user/cart?error=true");
            }
        }
    }

}