// model/OrderItem.java
package model;

public class OrderItem {
    private String bookTitle;
    private int quantity;
    private int price;

    public OrderItem(String bookTitle, int quantity, int price) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }

    // getter, setter
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
