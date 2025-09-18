package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import constant.PathConstants;
import dao.CartDAO;
import model.CartItem;
import model.User;

@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user"); // giả sử login đã có
        Long userId = user.getId();
        try {
            List<CartItem> cart = cartDAO.getCartByUser(userId.intValue());
            req.setAttribute("cart", cart);
            req.setAttribute("contentPage", PathConstants.VIEW_CART);
            req.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        Long userIdLong = (Long) req.getSession().getAttribute("userId");
        int userId = userIdLong != null ? userIdLong.intValue() : 0;

        try {
            if ("add".equals(action)) {
                int bookId = Integer.parseInt(req.getParameter("bookId"));
                cartDAO.addToCart(userId, bookId, 1);
            } else if ("remove".equals(action)) {
                int cartId = Integer.parseInt(req.getParameter("cartId"));
                cartDAO.removeFromCart(cartId);
            }
            resp.sendRedirect(PathConstants.URL_CART);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}
