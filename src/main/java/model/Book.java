package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Book implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private int categoryId;
    private String thumbnailUrl;
    private String description;
    private int stock;
    private Integer publishYear;
    private Integer pages;
    private double rating;
    private double price;
    private double originalPrice;
    private int discount_rate;
    private Timestamp createdAt;

    public Book() {
    }

    public Book(Integer id, String title, String author, double price, String publisher, int categoryId, int stock, double originalPrice, int discount_rate, String imageUrl, String description, Integer publishYear, Integer pages, double rating, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.thumbnailUrl = thumbnailUrl;
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getthumbnailUrl() { return thumbnailUrl; }
    public void setthumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public int getDiscount_rate() { return discount_rate; }
    public void setDiscount_rate(int discount_rate) { this.discount_rate = discount_rate; }

}
