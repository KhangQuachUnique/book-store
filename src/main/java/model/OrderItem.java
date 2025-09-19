package model;

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
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

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
