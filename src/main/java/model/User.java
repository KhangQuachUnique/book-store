package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;

/**
 * User entity representing users table in the database.
 * Contains user information including authentication, profile, and verification data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    private String passwordHash;
    private String phone;
    private String role; // 'customer' or 'admin'
    private String avatarUrl;
    
    // Status fields
    private Boolean isBlocked;
    private Timestamp blockedUntil;
    
    // Verification fields
    private Boolean isVerified;
    private String verifyToken;
    private Timestamp verifyExpire;
    
    // Audit fields
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /**
     * Returns a safe version of the user without sensitive information (passwordHash).
     * Used for API responses and client-side operations.
     * 
     * @return User object without passwordHash field
     */
    public User safeUser() {
        User safe = new User();
        safe.setId(this.id);
        safe.setName(this.name);
        safe.setEmail(this.email);
        safe.setPhone(this.phone);
        safe.setRole(this.role);
        safe.setAvatarUrl(this.avatarUrl);
        safe.setIsBlocked(this.isBlocked);
        safe.setBlockedUntil(this.blockedUntil);
        safe.setIsVerified(this.isVerified);
        safe.setVerifyToken(this.verifyToken);
        safe.setVerifyExpire(this.verifyExpire);
        safe.setCreatedAt(this.createdAt);
        safe.setUpdatedAt(this.updatedAt);
        // Explicitly set passwordHash to null for security
        safe.setPasswordHash(null);
        return safe;
    }
}
