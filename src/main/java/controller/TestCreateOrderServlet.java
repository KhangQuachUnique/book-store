//package controller;
//
//import java.io.IOException;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//
//import model.*;
//import service.OrderService;
//
//@WebServlet("/test/create-order")
//public class TestCreateOrderServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        resp.setContentType("text/plain;charset=UTF-8");
//        OrderService orderService = new OrderService();
//
//        try {
//            User user = new User();
//            user.setId(101L);
//
//            Promotion promo = new Promotion();
//            promo.setId(1L);
//            promo.setDiscount(10);
//            promo.setCode("SALE10");
//
//            Book b1 = new Book();
//            b1.setId(390569);
//            b1.setTitle("Tâm Lý Học Tội Phạm");
//            b1.setOriginalPrice(46000);
//            b1.setDiscountRate(5);
//
//            Book b2 = new Book();
//            b2.setId(315886);
//            b2.setTitle("Kỹ Năng Lãnh Đạo");
//            b2.setOriginalPrice(233000);
//            b2.setDiscountRate(0);
//
//            OrderItem item1 = new OrderItem();
//            item1.setBook(b1);
//            item1.setQuantity(3);
//
//            OrderItem item2 = new OrderItem();
//            item2.setBook(b2);
//            item2.setQuantity(1);
//
//            Order order = new Order();
//            order.setUser(user);
//            order.setPromotion(promo);
//            order.setItems(List.of(item1, item2));
//            order.setPaymentMethod("Thanh toán qua ZaloPay");
//            order.setStatus(OrderStatus.PROCESSING);
//
//            orderService.createOrder(order);
//
//            resp.getWriter().println("✅ Đã tạo đơn hàng test thành công!");
//            resp.getWriter().println("Tổng tiền sau giảm: " + order.getTotalAmount() + " VNĐ");
//            resp.getWriter().println("Khuyến mãi áp dụng: " + promo.getCode() + " (-" + promo.getDiscount() + "%)");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.getWriter().println("❌ Lỗi khi tạo đơn hàng test: " + e.getMessage());
//        }
//    }
//}
