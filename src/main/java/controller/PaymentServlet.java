//package controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import constant.PathConstants;
//import dao.CartDAO;
//import model.Address;
//import model.CartItem;
//import model.User;
//import service.AddressService;
//
//@WebServlet("/user/payment")
//public class PaymentServlet extends HttpServlet {
//    private CartDAO cartDAO = new CartDAO();
//    private AddressService addressService = new AddressService();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            response.sendRedirect(request.getContextPath() + "/login.jsp");
//            return;
//        }
//        try {
//            List<CartItem> cart = cartDAO.getCartByUser(user.getId().intValue());
//            double cartTotal = 0.0;
//            if (cart != null && !cart.isEmpty()) {
//                cartTotal = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
//            }
//            List<Address> addresses = addressService.getAddressesByUserId(user.getId());
//            request.setAttribute("cart", cart);
//            request.setAttribute("cartTotal", cartTotal);
//            request.setAttribute("addresses", addresses);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "An error occurred while loading your payment page.");
//        }
//        request.setAttribute("contentPage", "/WEB-INF/views/payment.jsp");
//        request.getRequestDispatcher(PathConstants.VIEW_LAYOUT).forward(request, response);
//    }
//}
