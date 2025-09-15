// Package: model
// Các sửa đổi chính: Thêm ràng buộc validation @NotBlank và @Email cho các trường quan trọng để đảm bảo dữ liệu hợp lệ ngay tại model.
package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "Name cannot be empty")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")

    private Long id;              // BIGSERIAL
    private String name;          // name
    private String email;         // email
    private String passwordHash;  // password_hash
    private String phone;         // phone
    private String role;          // 'customer' hoặc 'admin'
    private Boolean isBlocked;    // có bị block không
    private Timestamp blockedUntil;  // thời điểm hết block
    private Timestamp createdAt;
    private Timestamp updatedAt;
    // Thêm cho xác thực email
    private Boolean isVerified;      // đã xác thực chưa
    private String verifyToken;      // mã token
    private Timestamp verifyExpire;  // thời điểm hết hạn token
}