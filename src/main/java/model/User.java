// Package: model
// Các sửa đổi chính: Thêm ràng buộc validation @NotBlank và @Email cho các trường quan trọng để đảm bảo dữ liệu hợp lệ ngay tại model.
package model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id; // BIGSERIAL, tự động tăng

    @NotBlank(message = "Name cannot be empty")
    private String name; // name

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email; // email

    private String passwordHash; // password_hash, không validate tại model

    private String phone; // phone

    private List<Address> addresses;

    private String role; // 'customer' hoặc 'admin'

    private Boolean isBlocked; // có bị block không

    private Timestamp blockedUntil; // thời điểm hết block

    private Timestamp createdAt;

    private Timestamp updatedAt;

    // Thêm cho xác thực email
    private Boolean isVerified; // đã xác thực chưa
    private String verifyToken; // mã token
    private Timestamp verifyExpire; // thời điểm hết hạn token

    /**
     * Trả về bản User "safe", không có passwordHash
     */
    public User safeUser() {
        User safe = new User();
        safe.setId(this.id);
        safe.setName(this.name);
        safe.setEmail(this.email);
        safe.setPhone(this.phone);
        safe.setRole(this.role);
        safe.setIsBlocked(this.isBlocked);
        safe.setBlockedUntil(this.blockedUntil);
        safe.setCreatedAt(this.createdAt);
        safe.setUpdatedAt(this.updatedAt);
        safe.setIsVerified(this.isVerified);
        safe.setVerifyToken(this.verifyToken);
        safe.setVerifyExpire(this.verifyExpire);
        safe.setAddresses(this.addresses);

        // Không set passwordHsh
        safe.setPasswordHash(null);
        return safe;
    }
}
