package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
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

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"promotionId\"")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"status_id\"")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Transient
    private Long statusIdCache;

    @Transient
    private String statusNameCache;

    public Long getStatusId() {
        if (status != null) {
            return status.getId();
        }
        return statusIdCache;
    }

    public void setStatusId(Long statusId) {
        this.statusIdCache = statusId;
        if (statusId == null) {
            this.status = null;
            return;
        }
        if (this.status == null) {
            this.status = new OrderStatus();
        }
        this.status.setId(statusId);
    }

    public String getStatusName() {
        if (status != null) {
            return status.getName();
        }
        return statusNameCache;
    }

    public void setStatusName(String statusName) {
        this.statusNameCache = statusName;
        if (status == null) {
            status = new OrderStatus();
        }
        status.setName(statusName);
    }

    public void setCreatedAt(String createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
            return;
        }
        try {
            this.createdAt = Timestamp.valueOf(createdAt);
        } catch (IllegalArgumentException ex) {
            this.createdAt = Timestamp.from(Instant.parse(createdAt));
        }
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.statusIdCache = status != null ? status.getId() : null;
        this.statusNameCache = status != null ? status.getName() : null;
    }
}