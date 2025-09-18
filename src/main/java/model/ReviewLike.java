package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * ReviewLike entity representing review_likes table in the database.
 * Contains user likes for reviews (many-to-many relationship).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {
    
    private Long id;
    private Long reviewId;
    private Long userId;
}