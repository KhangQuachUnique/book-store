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
@Table(name = "\"viewedProductItems\"")
public class ViewedProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"bookId\"", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"viewedProductId\"", nullable = false)
    private ViewedProduct viewedProduct;

    @Transient
    private Timestamp viewedAt;

    public ViewedProductItem(Long id, Book book, Timestamp viewedAt) {
        this.id = id;
        this.book = book;
        this.viewedAt = viewedAt;
    }
}
