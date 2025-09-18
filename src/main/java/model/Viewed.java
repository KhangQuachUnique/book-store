package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * Viewed entity representing viewed table in the database.
 * Contains user's book viewing history for recommendations and analytics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viewed {
    
    private Long id;
    private Long userId;
    private Long bookId;
    private Timestamp viewedAt;
}