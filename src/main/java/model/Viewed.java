package model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Viewed entity representing viewed table in the database. Contains user's book viewing history for
 * recommendations and analytics.
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
