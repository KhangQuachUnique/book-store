package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Book")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"categoryId\"", nullable = false)
    private Category category;

    @Column(name = "\"stock\"")
    @Min(value = 0, message = "Stock must be non-negative")
    private Integer stock;

    @Column(name = "\"originalPrice\"")
    private Double originalPrice;

    @Column(name = "\"discountRate\"")
    @Min(value = 0, message = "Discount rate must be non-negative")
    @Max(value = 100, message = "Discount rate must not exceed 100")
    private Integer discountRate;

    @Column(name = "\"thumbnailUrl\"")
    private String thumbnailUrl;

    @Column(name = "\"description\"")
    private String description;

    @Column(name = "\"publishYear\"")
    private Integer publishYear;

    @Column(name = "\"pages\"")
    private Integer pages;

    @Column(name = "\"averageRating\"")
    private Double averageRating;

    @Column(name = "\"sold\"")
    private Integer sold;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishListItem> wishlistItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ViewedProductItem> viewedProductItems;

    @Transient
    private Double price;

    public Double getPrice() {
        if (originalPrice == null || discountRate == null) {
            return 0.0;
        }
        return originalPrice * (100 - discountRate) / 100.0;
    }

    public Integer getSold() {
        return sold != null ? sold : 0;
    }

    public void setSold(Integer sold) {
        this.sold = (sold != null && sold >= 0) ? sold : 0;
    }

    public String getAuthor() {
        return author != null ? author : "";
    }

    public String getPublisher() {
        return publisher != null ? publisher : "";
    }

    public String getThumbnailUrl() {
        return thumbnailUrl != null ? thumbnailUrl : "";
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public Integer getStock() {
        return stock != null ? stock : 0;
    }

    public Double getOriginalPrice() {
        return originalPrice != null ? originalPrice : 0.0;
    }

    public Integer getDiscountRate() {
        return discountRate != null ? discountRate : 0;
    }

    public Double getAverageRating() {
        return averageRating != null ? averageRating : 0.0;
    }
}