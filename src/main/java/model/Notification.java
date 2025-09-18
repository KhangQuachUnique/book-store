package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String message;
    private String type; // "EMAIL" or "IN_APP"
    private Timestamp createdAt;
    private Boolean isRead;
}