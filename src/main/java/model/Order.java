package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"orders\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"paymentMethod\"")
    private String paymentMethod;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    @Column(name = "\"totalAmount\"")
    private double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"promotionId\"")
    @ToString.Exclude
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"address\"")
    @ToString.Exclude
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"status\"")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OrderItem> items;

    // ✅ Các trường chỉ để hiển thị (không lưu DB)
    @Transient
    private double subtotal;

    @Transient
    private double discountAmount;

    @Transient
    private double finalTotal;

    // ✅ Hàm tiện ích: set tổng tiền tự động
    public void calculateTotals() {
        if (items != null) {
            this.subtotal = items.stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
        }
        if (promotion != null) {
            this.discountAmount = subtotal * promotion.getDiscount() / 100.0;
        } else {
            this.discountAmount = 0;
        }
        this.finalTotal = subtotal - discountAmount;
        this.totalAmount = finalTotal; // để đồng bộ field DB
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }
}