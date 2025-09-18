package model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResult {
    public enum LoginStatus {
        SUCCESS, // đăng nhập thành công
        UNVERIFIED, // chưa kích hoạt tài khoản
        INVALID, // sai email hoặc mật khẩu
        BLOCKED // đang bị khóa
    }

    private User user;
    private LoginStatus status;
    private Timestamp blockedUntil; // chỉ dùng nếu BLOCKED
}
