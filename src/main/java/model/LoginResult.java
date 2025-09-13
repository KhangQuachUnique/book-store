package model;

import java.sql.Timestamp;

public class LoginResult {
    public enum LoginStatus {
        SUCCESS,
        INVALID,   // sai email hoặc mật khẩu
        BLOCKED    // đang bị khóa
    }

    private User user;
    private LoginStatus status;
    private Timestamp blockedUntil; // chỉ dùng nếu BLOCKED

    // getters & setters
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public LoginStatus getStatus() {
        return status;
    }
    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public Timestamp getBlockedUntil() {
        return blockedUntil;
    }
    public void setBlockedUntil(Timestamp blockedUntil) {
        this.blockedUntil = blockedUntil;
    }
}
