package model;

<<<<<<< HEAD
import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
    private Book book;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
=======
public class OrderItem {
    private String bookTitle;
    private int quantity;
    private double price;
    private String thumbnailUrl;
    private double originalPrice;
    private int discountRate;

    public OrderItem() {
    }

    public OrderItem(String bookTitle, int quantity, double price, String thumbnailUrl, double originalPrice,
            int discountRate) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
    }

    // Getters & Setters
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

<<<<<<< HEAD
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
=======
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
}
>>>>>>> ce462613b685cbdad207495480b6c9df022f730d
