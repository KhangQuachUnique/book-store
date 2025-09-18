package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * Notification entity representing notifications table in the database.
 * Contains user notifications with type and read status tracking.
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