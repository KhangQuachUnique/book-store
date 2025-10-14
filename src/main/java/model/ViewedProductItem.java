package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "\"viewedProductItems\"",
        uniqueConstraints = @UniqueConstraint(columnNames = {"\"bookId\"", "\"viewedProductId\""})
)
public class ViewedProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"bookId\"", nullable = false)
    private Book book;

    @Column(name = "\"viewedAt\"", nullable = false)
    private Timestamp viewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"viewedProductId\"", nullable = false)
    private ViewedProduct viewedProduct;

    @PrePersist
    private void prePersist() {
        if (this.viewedAt == null) {
            this.viewedAt = new Timestamp(System.currentTimeMillis());
        }
    }
}
