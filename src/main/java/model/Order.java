package model;

import java.util.List;

public class Order {
    private Long id;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

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
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
