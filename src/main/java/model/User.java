// Package: model
// Các sửa đổi chính: Thêm ràng buộc validation @NotBlank và @Email cho các trường quan trọng để đảm bảo dữ liệu hợp lệ ngay tại model.
// Thêm @Column cho phoneNumber mapping đến cột "phoneNumber" trong DB với quotes.
// Thêm @PrePersist và @PreUpdate để tự động set createdAt/updatedAt.
// Thêm enum Role.
// Sử dụng quotes cho tất cả tên cột trong @Column để phù hợp với schema DB.
// Điều chỉnh các trường Boolean để null-safe.
// Thêm import cho lifecycle callbacks và time.
package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"users\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "\"name\"", nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(name = "\"email\"", nullable = false, unique = true)
    private String email;

    @Column(name = "\"passwordHash\"", nullable = false)
    private String passwordHash;

    @Column(name = "\"phoneNumber\"")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"role\"", length = 20, nullable = false)
    private Role role;

    @Column(name = "\"avatarUrl\"")
    private String avatarUrl;

    @Column(name = "\"isBlocked\"")
    private Boolean isBlocked;

    @Column(name = "\"blockedUntil\"")
    private Timestamp blockedUntil;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;

    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

    @Column(name = "\"isVerified\"")
    private Boolean isVerified;

    @Column(name = "\"verifyToken\"")
    private String verifyToken;

    @Column(name = "\"verifyExpire\"")
    private Timestamp verifyExpire;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private WishList wishList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ViewedProduct viewedProduct;

    @PrePersist
    private void prePersist() {
        Timestamp now = new Timestamp(Instant.now().toEpochMilli());
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (isBlocked == null) {
            isBlocked = false;
        }
        if (isVerified == null) {
            isVerified = false;
        }
        if (role == null) {
            role = Role.USER; // Set default role for new users
        }
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = new Timestamp(Instant.now().toEpochMilli());
    }

    /**
     * Trả về bản User "safe", không có passwordHash
     */
    public User safeUser() {
        User safe = new User();
        safe.setId(this.id);
        safe.setName(this.name);
        safe.setEmail(this.email);
        safe.setPhoneNumber(this.phoneNumber);
        safe.setRole(this.role);
        safe.setAvatarUrl(this.avatarUrl);
        safe.setIsBlocked(this.isBlocked);
        safe.setBlockedUntil(this.blockedUntil);
        safe.setCreatedAt(this.createdAt);
        safe.setUpdatedAt(this.updatedAt);
        safe.setIsVerified(this.isVerified);
        safe.setVerifyToken(this.verifyToken);
        safe.setVerifyExpire(this.verifyExpire);
        safe.setAddresses(this.addresses);

        // Không set passwordHash
        safe.setPasswordHash(null);
        return safe;
    }
}
