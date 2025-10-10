// package controller;

// import java.io.IOException;
// import java.util.List;

// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import constant.PathConstants;
// import dao.CartDAO;
// import model.CartItem;
// import model.User;

// @WebServlet("/user/cart")
// public class CartServlet extends HttpServlet {
//     private final CartDAO cartDAO = new CartDAO();

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//         User user = (User) req.getSession().getAttribute("user");

//         // Check if user is logged in
//         if (user == null) {
//             req.setAttribute("contentPage", PathConstants.VIEW_PLEASE_LOGIN);
//             req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//             return;
//         }

//         try {
//             // Set content page first to ensure it's never null
//             req.setAttribute("contentPage", PathConstants.VIEW_CART);

//             // Check for error parameter
//             if ("true".equals(req.getParameter("error"))) {
//                 req.setAttribute("error", "An error occurred while updating your cart.");
//             }

//             List<CartItem> cart = cartDAO.getCartByUser(user.getId().intValue());

//             // Initialize cart total
//             double cartTotal = 0.0;

//             // Calculate totals only if cart is not empty
//             if (cart != null && !cart.isEmpty()) {
//                 cartTotal = cart.stream()
//                         .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                         .sum();
//             }

//             req.setAttribute("cartTotal", cartTotal);

//             req.setAttribute("cart", cart);
//             req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//         } catch (Exception e) {
//             e.printStackTrace();
//             // Even in case of error, set a content page
//             req.setAttribute("contentPage", PathConstants.VIEW_CART);
//             req.setAttribute("error", "An error occurred while loading your cart.");
//             req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
//         }
//     }

//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//         String action = req.getParameter("action");

//         User user = (User) req.getSession().getAttribute("user");
//         if (user == null) {
//             if ("update".equals(action)) {
//                 resp.setContentType("application/json");
//                 resp.setStatus(401);
//                 resp.getWriter().write("{\"success\":false,\"message\":\"Please log in\"}");
//             } else {
//                 resp.sendRedirect(req.getContextPath() + "/login.jsp");
//             }
//             return;
//         }
//         int userId = user.getId().intValue();

//         try {
//             if ("add".equals(action)) {
//                 int bookId = Integer.parseInt(req.getParameter("bookId"));
//                 cartDAO.addToCart(userId, bookId, 1);
//             } else if ("remove".equals(action)) {
//                 int cartId = Integer.parseInt(req.getParameter("cartId"));
//                 cartDAO.removeFromCart(cartId);
//             } else if ("update".equals(action)) {
//                 int cartId = Integer.parseInt(req.getParameter("cartId"));
//                 int quantity = Integer.parseInt(req.getParameter("quantity"));
//                 cartDAO.updateCartQuantity(cartId, quantity);

//                 // Get updated cart data
//                 List<CartItem> cart = cartDAO.getCartByUser(userId);
//                 double cartTotal = cart.stream()
//                         .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                         .sum();
//                 CartItem updatedItem = cart.stream()
//                         .filter(item -> item.getId() == cartId)
//                         .findFirst()
//                         .orElse(null);

//                 resp.setContentType("application/json");
//                 resp.getWriter().write(String.format(
//                         "{\"success\":true,\"itemTotal\":%.0f,\"cartTotal\":%.0f}",
//                         updatedItem != null ? updatedItem.getPrice() * updatedItem.getQuantity() : 0,
//                         cartTotal));
//             }
//             if (!"update".equals(action)) {
//                 resp.sendRedirect(req.getContextPath() + "/user/cart");
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//             if ("update".equals(action)) {
//                 resp.setContentType("application/json");
//                 resp.setStatus(500);
//                 resp.getWriter().write("{\"success\":false,\"message\":\"An error occurred\"}");
//             } else {
//                 resp.sendRedirect(req.getContextPath() + "/user/cart?error=true");
//             }
//         }
//     }

// }
