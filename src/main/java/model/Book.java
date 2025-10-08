package model;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

// ✅ Đổi từ javax → jakarta
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Model class representing a book.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Integer id;

    @NotNull(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(name = "\"title\"", nullable = false)
    private String title;

    @Column(name = "\"author\"")
    private String author;

    @Column(name = "\"publisher\"")
    private String publisher;

    @Column(name = "\"thumbnailUrl\"")
    private String thumbnailUrl;

    @Column(name = "\"description\"")
    private String description;

    @Column(name = "\"publishYear\"")
    private Integer publishYear;

    @Column(name = "\"pages\"")
    private Integer pages;

    @Column(name = "\"originalPrice\"")
    private double originalPrice;

    @Column(name = "\"discountRate\"")
    @Min(value = 0, message = "Discount rate must be non-negative")
    @Max(value = 100, message = "Discount rate must not exceed 100")
    private int discountRate;

    @Column(name = "\"stock\"")
    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;

    @Column(name = "\"rating\"")
    @Min(value = 0, message = "Rating must be non-negative")
    @Max(value = 5, message = "Rating must not exceed 5")
    private double rating;

    @Transient
    private Integer fullStars;
    @Transient
    private Double partialFraction;
    @Transient
    private Integer emptyStars;

    @Column(name = "\"price\"")
    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"categoryId\"", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishListItem> wishlistItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ViewedProductItem> viewedProductItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LikeReview> likeReviews;

    public void calculateStars() {
        this.fullStars = (int) rating;
        this.partialFraction = rating - fullStars;
        this.emptyStars = (partialFraction > 0) ? (4 - fullStars) : (5 - fullStars);
    }
}
