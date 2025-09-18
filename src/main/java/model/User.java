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


    private Long id; // BIGSERIAL PRIMARY KEY

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    private String passwordHash;
    private String phone;
    private String role; // 'customer' or 'admin'
    private Boolean isBlocked;
    private Timestamp blockedUntil;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isVerified;
    private String verifyToken;
    private Timestamp verifyExpire;
    // Add avatarUrl if present in schema
    private String avatarUrl;

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
        safe.setAvatarUrl(this.avatarUrl);
        // Không set passwordHash
        safe.setPasswordHash(null);
        return safe;
    }
}
