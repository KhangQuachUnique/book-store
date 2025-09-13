package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private String role;
    private Boolean isBlocked;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp blockedUntil;
}