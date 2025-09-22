package model;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.sql.Timestamp;
=======
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
import java.util.List;

public class Order {
    private Long id;
<<<<<<< HEAD
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
=======
    private String createdAt;
    private double totalAmount; // có thể bỏ, vì JSP tự tính
    private String paymentMethod;

    private Long statusId;
    private String statusName;

    private List<OrderItem> items;

    public Order() {
    }

    public Order(Long id, String createdAt, double totalAmount, String paymentMethod, Long statusId, String statusName) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.statusId = statusId;
        this.statusName = statusName;
    }

    // Getters & Setters
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

<<<<<<< HEAD
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

=======
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

<<<<<<< HEAD
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
=======
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
<<<<<<< HEAD

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
=======
}
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
