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

@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CartServlet.class.getName());
    private static final long SHIPPING_FEE = 30_000L;

    private final CartService cartService = new CartService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        try {
            req.setAttribute("contentPage", PathConstants.VIEW_CART);

            if ("true".equals(req.getParameter("error"))) {
                req.setAttribute("error", "An error occurred while updating your cart.");
            }

            Cart cart = cartService.getCart(user.getId());
            List<CartItem> items = cart != null && cart.getItems() != null ? cart.getItems() : Collections.emptyList();

            double cartTotal = cartService.calculateSubtotal(items);
            List<Address> addresses = addressService.getAddressesByUserId(user.getId());

            req.setAttribute("cartTotal", cartTotal);
            req.setAttribute("shippingFee", SHIPPING_FEE);
            req.setAttribute("cart", items);
            req.setAttribute("addresses", addresses);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to load cart", e);
            req.setAttribute("contentPage", PathConstants.VIEW_CART);
            req.setAttribute("error", "An error occurred while loading your cart.");
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        User user = (User) req.getSession().getAttribute("user");
        Long userId = user.getId();

        try {
            if ("add".equals(action)) {
                Long bookId = Long.parseLong(req.getParameter("bookId"));
                int quantity = 1;
                if (req.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(req.getParameter("quantity"));
                }
                cartService.addToCart(userId, bookId, quantity);
                
                // Check if this is an AJAX request (look for X-Requested-With header or ajax parameter)
                String requestedWith = req.getHeader("X-Requested-With");
                String ajax = req.getParameter("ajax");
                if ("XMLHttpRequest".equals(requestedWith) || "true".equals(ajax)) {
                    // AJAX request - return JSON response
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"success\":true,\"message\":\"Book added to cart successfully!\"}");
                    return;
                }
            } else if ("remove".equals(action)) {
                Long cartId = Long.parseLong(req.getParameter("cartId"));
                cartService.removeItem(userId, cartId);
            } else if ("update".equals(action)) {
                Long cartId = Long.parseLong(req.getParameter("cartId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                CartService.CartUpdateResult result = cartService.updateItemQuantity(userId, cartId, quantity);

                resp.setContentType("application/json");
                resp.getWriter().write(String.format(Locale.US,
                        "{\"success\":true,\"itemTotal\":%.0f,\"cartTotal\":%.0f}",
                        result.itemTotal(),
                        result.cartTotal()));
                return;
            }
            if (!"update".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/user/cart");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Cart action failed", e);
            
            // Check if this is an AJAX request for error handling
            String requestedWith = req.getHeader("X-Requested-With");
            String ajax = req.getParameter("ajax");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith) || "true".equals(ajax);
            
            if ("update".equals(action) || isAjax) {
                resp.setContentType("application/json");
                resp.setStatus(500);
                resp.getWriter().write("{\"success\":false,\"message\":\"An error occurred while processing your request\"}");
            } else {
                resp.sendRedirect(req.getContextPath() + "/user/cart?error=true");
            }
        }
    }

}