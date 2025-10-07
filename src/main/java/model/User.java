// Package: model
// Các sửa đổi chính: Thêm ràng buộc validation @NotBlank và @Email cho các trường quan trọng để đảm bảo dữ liệu hợp lệ ngay tại model.
package model;

import java.sql.Timestamp;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String passwordHash;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
    private String phone;

    private List<Address> addresses;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private WishList wishList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addressList;

    private Boolean isBlocked;
    private Timestamp blockedUntil;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isVerified;
    private String verifyToken;
    private Timestamp verifyExpire;

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
