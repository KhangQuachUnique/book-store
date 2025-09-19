package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Book implements Serializable {
    private Integer id;

    @NotNull(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    private String author;
    private String publisher;

    @Min(value = 1, message = "Category ID must be positive")
    private int categoryId;

    private String thumbnailUrl;
    private String description;

    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;

    private Integer publishYear;

    @Min(value = 1, message = "Pages must be positive")
    private Integer pages;

    @Min(value = 0, message = "Rating must be non-negative")
    @Max(value = 5, message = "Rating must not exceed 5")
    private double rating;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @Min(value = 0, message = "Original price must be non-negative")
    private double originalPrice;

    @Min(value = 0, message = "Discount rate must be non-negative")
    @Max(value = 100, message = "Discount rate must not exceed 100")
    private int discount_rate;

    private Timestamp createdAt;

    public Book() {
    }

    public Book(Integer id, String title, String author, double price, String publisher, int categoryId, int stock,
            double originalPrice, int discount_rate, String imageUrl, String description, Integer publishYear,
            Integer pages, double rating, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.thumbnailUrl = imageUrl;
        this.description = description;
        this.publishYear = publishYear;
        this.pages = pages;
        this.rating = rating;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.stock = stock;
        this.originalPrice = originalPrice;
        this.discount_rate = discount_rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getthumbnailUrl() {
        return thumbnailUrl;
    }

    public void setthumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

}
