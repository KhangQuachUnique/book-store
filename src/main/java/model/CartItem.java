package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"cartItems\"", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cart_book", columnNames = {"\"cartId\"", "\"bookId\""})
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"quantity\"", nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Column(name = "\"unitPrice\"")
    private Double unitPrice;

    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"bookId\"", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"cartId\"", nullable = false)
    private Cart cart;

    @PrePersist
    private void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
        if (this.unitPrice == null && this.book != null) {
            this.unitPrice = this.book.getPrice();
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public void increaseQuantity(int delta) {
        this.quantity = Math.max(1, this.quantity + delta);
    }

    public void setQuantitySafely(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public double getEffectiveUnitPrice() {
        if (unitPrice != null) {
            return unitPrice;
        }
        if (book != null) {
            unitPrice = book.getPrice();
            return unitPrice;
        }
        return 0D;
    }

    public double getSubtotal() {
        return getEffectiveUnitPrice() * this.quantity;
    }
}