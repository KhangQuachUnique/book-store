package model;

import java.sql.Timestamp;

public class ViewHistoryItem {
    private Long id;
    private Long bookId;
    private String title;
    private String thumbnail;
    private Timestamp viewedAt;

    public ViewHistoryItem() {}

    public ViewHistoryItem(Long id, Long bookId, String title, String thumbnail, Timestamp viewedAt) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.viewedAt = viewedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Timestamp getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Timestamp viewedAt) {
        this.viewedAt = viewedAt;
    }
}