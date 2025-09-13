package model;

public class OrderItem {
    private String bookTitle;  // ✅ Thêm trường này
    private int quantity;
    private double price;
    private String thumbnailUrl;

    // ✅ Constructor rỗng
    public OrderItem() {}

    // Constructor đầy đủ (nếu cần)
    public OrderItem(String bookTitle, int quantity, double price, String thumbnailUrl) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getter & Setter
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
}
