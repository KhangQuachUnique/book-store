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

    @Column(name = "\"averageRating\"")
    private Double averageRating;

    @Column(name = "\"sold\"")
    private Integer sold;

    @Column(name = "\"originalPrice\"")
    private double originalPrice;

    @Column(name = "\"discountRate\"")
    @Min(value = 0, message = "Discount rate must be non-negative")
    @Max(value = 100, message = "Discount rate must not exceed 100")
    private Integer discountRate;

    @Column(name = "\"stock\"")
    @Min(value = 0, message = "Stock must be non-negative")
    private Integer stock;

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


    // Transient fields
    @Transient
    private Double price;

    @Transient
    private Integer categoryIdCache;

    @Transient
    private Integer fullStars;

    @Transient
    private Double fractionalStars;

    @Transient
    private Integer emptyStars;

    public Double getPrice() {
        if (price != null) {
            return price;
        }
        double effectiveDiscount = discountRate != null ? discountRate : 0;
        price = originalPrice * (100 - effectiveDiscount) / 100;
        return price;
    }

    public Double getRating() {
        return averageRating;
    }

    public void setRating(Double rating) {
        this.averageRating = rating;
    }

    public Integer getDiscount_rate() {
        return discountRate != null ? discountRate : 0;
    }

    public void setDiscount_rate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Integer getCategoryId() {
        if (category != null) {
            return (int) category.getId();
        }
        return categoryIdCache;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryIdCache = categoryId;
        if (categoryId == null) {
            this.category = null;
            return;
        }
        if (this.category == null) {
            this.category = new Category();
        }
        this.category.setId(categoryId);
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryIdCache = category != null ? (int) category.getId() : null;
    }

    public void calculateStars() {
        if (averageRating == null) {
            fullStars = 0;
            fractionalStars = 0.0;
            emptyStars = 5;
            return;
        }
        fullStars = (int) Math.floor(averageRating);
        fractionalStars = averageRating - fullStars;
        emptyStars = Math.max(0, 5 - fullStars - (fractionalStars > 0 ? 1 : 0));
    }

    public Integer getFullStars() {
        if (fullStars == null) {
            calculateStars();
        }
        return fullStars;
    }

    public Double getFractionalStars() {
        if (fractionalStars == null) {
            calculateStars();
        }
        return fractionalStars;
    }

    public Integer getEmptyStars() {
        if (emptyStars == null) {
            calculateStars();
        }
        return emptyStars;
    }
}
