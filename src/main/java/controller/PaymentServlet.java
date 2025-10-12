package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.PathConstants;
import model.Address;
import model.Cart;
import model.CartItem;
import model.User;
import service.AddressService;
import service.CartService;
import service.PromotionService;
import model.Promotion;

@WebServlet("/user/payment")
public class PaymentServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(PaymentServlet.class.getName());

    private final CartService cartService = new CartService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            Cart cart = cartService.getCart(user.getId());
            List<CartItem> items = cart != null && cart.getItems() != null ? cart.getItems() : Collections.emptyList();
            double cartTotal = cartService.calculateSubtotal(items);
            List<Address> addresses = addressService.getAddressesByUserId(user.getId());

            PromotionService promotionService = new PromotionService();
            List<Promotion> validPromotions = promotionService.getAllValidPromotions();
            request.setAttribute("validPromotions", validPromotions);

            request.setAttribute("cart", items);
            request.setAttribute("cartTotal", cartTotal);
            request.setAttribute("addresses", addresses);
            request.setAttribute("shippingFee", cartService.getShippingFee());

            String resultCode = request.getParameter("resultCode");
            if (resultCode != null) {
                String message = request.getParameter("message");
                if ("0".equals(resultCode)) {
                    request.setAttribute("paymentSuccess", true);
                    request.setAttribute("paymentMessage", "Thanh toán MoMo thành công.");
                } else {
                    request.setAttribute("paymentSuccess", false);
                    request.setAttribute("paymentMessage", message != null ? message : "Thanh toán MoMo thất bại. Vui lòng thử lại.");
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to load payment page", e);
            request.setAttribute("error", "An error occurred while loading your payment page.");
        }
        request.setAttribute("contentPage", "/WEB-INF/views/payment.jsp");
        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
    }
}