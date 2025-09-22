package model;

import java.io.Serializable;

public class WishListItem implements Serializable {
    private int userId;
    private Book book;
    private String addedAt;

    public WishListItem() {
    }

    public WishListItem(int userId, Book book, String addedAt) {
        this.userId = userId;
        this.book = book;
        this.addedAt = addedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }
}
