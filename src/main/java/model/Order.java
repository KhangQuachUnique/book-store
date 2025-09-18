package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private Integer id;
    private Integer userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private double totalAmount;
    private String status; // PENDING, CONFIRMED, DELIVERY, CANCELLED
    private String createdAt;
    private String updatedAt;
    private String notes;

    public Order() {
    }

    public Order(Integer id, Integer userId, String customerName, String customerEmail,
                 String customerPhone, String shippingAddress, double totalAmount,
                 String status, String createdAt, String updatedAt, String notes) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Utility methods for status
    public String getStatusBadgeClass() {
        switch (status) {
            case "PENDING": return "badge-warning";
            case "CONFIRMED": return "badge-info";
            case "DELIVERY": return "badge-success";
            case "CANCELLED": return "badge-danger";
            default: return "badge-secondary";
        }
    }

    public String getStatusDisplayName() {
        switch (status) {
            case "PENDING": return "Chờ xử lý";
            case "CONFIRMED": return "Đã xác nhận";
            case "DELIVERY": return "Đang giao hàng";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }
}
