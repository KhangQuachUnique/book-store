package model;

import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login result wrapper containing authentication status and user information. Used to communicate
 * login outcomes to the client.
 */
@Data
@NoArgsConstructor
public class LoginResult {

    /**
     * Enumeration of possible login statuses.
     */
    public enum LoginStatus {
        SUCCESS, // Login successful
        UNVERIFIED, // Account not yet verified
        INVALID, // Invalid email or password
        BLOCKED // Account is blocked
    }

    private User user;
    private LoginStatus status;
    private Timestamp blockedUntil; // Only used when status is BLOCKED
}
