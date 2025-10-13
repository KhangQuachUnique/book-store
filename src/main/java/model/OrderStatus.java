package model;

public enum OrderStatus {
    PENDING_PAYMENT,   // Chờ thanh toán
    PROCESSING,        // Chờ xử lý
    WAITING_DELIVERY,  // Chờ giao hàng
    DELIVERED,         // Đã giao
    CANCELED
}
