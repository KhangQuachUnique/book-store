package model;

import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private String userName;
    private String createdAt;
    private double totalAmount;
    private String paymentMethod;
    private Long statusId;
    private String statusName;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(Long id, Long userId, String userName, String createdAt, double totalAmount, String paymentMethod, Long statusId, String statusName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.statusId = statusId;
        this.statusName = statusName;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
