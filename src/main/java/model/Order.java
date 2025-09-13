package model;

import java.util.List;

public class Order {
    private int id;
    private String createdAt;
    private double totalAmount;
    private String status;
    private String paymentMethod;
    private List<OrderItem> items;

    // ✅ Constructor rỗng
    public Order() {}

    // Constructor đầy đủ (nếu cần)
    public Order(int id, String createdAt, double totalAmount, String status, String paymentMethod) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
