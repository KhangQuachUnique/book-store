package model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Transient
    private String bookTitle;

    @Transient
    private Double price;

    @Transient
    private String thumbnailUrl;

    @Transient
    private Double originalPrice;

    @Transient
    private Integer discountRate;

    public void setPrice(int price) {
        this.price = (double) price;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
}
