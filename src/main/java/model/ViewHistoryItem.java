package model;

import java.sql.Timestamp;

public class ViewHistoryItem {
    private Long id;
    private Book book;        // chứa toàn bộ thông tin Book
    private Timestamp viewedAt;

    public ViewHistoryItem() {}

    public ViewHistoryItem(Long id, Book book, Timestamp viewedAt) {
        this.id = id;
        this.book = book;
        this.viewedAt = viewedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Timestamp getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Timestamp viewedAt) {
        this.viewedAt = viewedAt;
    }
}
