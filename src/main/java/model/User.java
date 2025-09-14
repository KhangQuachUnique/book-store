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
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String passwordHash;

    private String phone;

    private String role;

    private Boolean isBlocked;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp blockedUntil;
}