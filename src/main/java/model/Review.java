package model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Review entity representing reviews table in the database. Contains user reviews for books with
 * rating and comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private Long id;
    private Long bookId;
    private Long userId;
    private Integer rating; // 1-5 stars
    private String comment;

    // Audit fields
    private Timestamp createdAt;
}
