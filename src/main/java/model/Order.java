package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private Long promotionId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User user;
    private List<OrderItem> items;

    // Status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PAID = "paid";
    public static final String STATUS_SHIPPED = "shipped";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";

    // Payment method constants
    public static final String PAYMENT_COD = "COD";
    public static final String PAYMENT_MOMO = "MOMO";
    public static final String PAYMENT_ZALOPAY = "ZALOPAY";
    public static final String PAYMENT_CARD = "CARD";

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // Validation method for status transitions
    public boolean isValidStatusTransition(String newStatus) {
        if (status == null) return true;
        
        switch (status) {
            case STATUS_PENDING:
                return newStatus.equals(STATUS_PAID) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_PAID:
                return newStatus.equals(STATUS_SHIPPED) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_SHIPPED:
                return newStatus.equals(STATUS_COMPLETED) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_COMPLETED:
            case STATUS_CANCELLED:
                return false;
            default:
                return false;
        }
    }
}