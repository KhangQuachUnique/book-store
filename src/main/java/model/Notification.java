package model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification entity representing notifications table in the database. Contains user notifications
 * with type and read status tracking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;
    private Long userId;
    private String message;
    private String type; // "EMAIL" or "IN_APP"
    private Boolean isRead;

    // Audit fields
    private Timestamp createdAt;
}
