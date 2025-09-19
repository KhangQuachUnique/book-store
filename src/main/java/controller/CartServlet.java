package controller;

import constant.PathConstants;
import dao.CartDAO;
import model.CartItem;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            req.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
            return;
        }

        try {
            // Set content page first to ensure it's never null
            req.setAttribute("contentPage", PathConstants.VIEW_CART);
            
            // Check for error parameter
            if ("true".equals(req.getParameter("error"))) {
                req.setAttribute("error", "An error occurred while updating your cart.");
            }
            
            List<CartItem> cart = cartDAO.getCartByUser(user.getId().intValue());
            
            // Initialize cart total
            double cartTotal = 0.0;
            
            // Calculate totals only if cart is not empty
            if (cart != null && !cart.isEmpty()) {
                cartTotal = cart.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            }
            
            req.setAttribute("cartTotal", cartTotal);
            
            req.setAttribute("cart", cart);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            // Even in case of error, set a content page
            req.setAttribute("contentPage", PathConstants.VIEW_CART);
            req.setAttribute("error", "An error occurred while loading your cart.");
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int userId = user.getId().intValue();

        try {
            if ("add".equals(action)) {
                int bookId = Integer.parseInt(req.getParameter("bookId"));
                cartDAO.addToCart(userId, bookId, 1);
            } else if ("remove".equals(action)) {
                int cartId = Integer.parseInt(req.getParameter("cartId"));
                cartDAO.removeFromCart(cartId);
            } else if ("update".equals(action)) {
                int cartId = Integer.parseInt(req.getParameter("cartId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cartDAO.updateCartQuantity(cartId, quantity);
            }

            // Redirect back to cart page after operation
            resp.sendRedirect(req.getContextPath() + "/user/cart");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/user/cart?error=true");
        }
    }

}
