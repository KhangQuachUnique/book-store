package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"carts\"")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

    @PrePersist
    private void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PostLoad
    private void ensureItemsList() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
    }

    public void addItem(CartItem item) {
        if (item == null) {
            return;
        }
        ensureItemsList();
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(CartItem item) {
        if (item == null || this.items == null) {
            return;
        }
        this.items.remove(item);
        item.setCart(null);
    }

    public void clearItems() {
        if (this.items == null) {
            return;
        }
        this.items.forEach(item -> item.setCart(null));
        this.items.clear();
    }
}