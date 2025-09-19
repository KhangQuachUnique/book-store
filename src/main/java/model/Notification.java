package model;

import java.sql.Timestamp;

public class Notification {
    private long id;
    private long userId;
    private String message;
    private String type; // "EMAIL" hoặc "IN_APP"
    private Timestamp createdAt;
    private boolean isRead; // Thêm trường is_read

    // Constructors
    public Notification() {
    }

    public Notification(long id, long userId, String message, String type, Timestamp createdAt, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
