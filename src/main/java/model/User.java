package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;              // BIGSERIAL
    private String name;          // name
    private String email;         // email
    private String passwordHash;  // password_hash
    private String phone;         // phone
    private String role;          // role: 'customer' or 'admin'
    private Boolean isBlocked;    // is_blocked
    private Timestamp blockedUntil;  // blocked_until
}
