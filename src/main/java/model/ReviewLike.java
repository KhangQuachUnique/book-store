package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReviewLike entity representing review_likes table in the database. Contains user likes for
 * reviews (many-to-many relationship).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {

    private Long id;
    private Long reviewId;
    private Long userId;
}
