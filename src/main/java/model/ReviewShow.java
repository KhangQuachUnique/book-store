package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

/**
 * ReviewShow is a display model for showing review information with user details.
 * Contains calculated star representation and like information for UI rendering.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewShow {
    
    private Long id;
    private Long userId;
    private String username;
    private String avatarUrl;
    private Double rating;
    private String comment;
    private Integer likeCount;
    private Timestamp date;
    private Boolean likedByCurrentUser;
    
    // Calculated star representation fields
    private Integer fullStars;
    private Double partialFraction;
    private Integer emptyStars;

    /**
     * Calculates star representation for UI display.
     * Splits rating into full stars, partial fraction, and empty stars.
     */
    public void calculateStars() {
        if (rating != null) {
            this.fullStars = rating.intValue();
            this.partialFraction = rating - fullStars;
            this.emptyStars = (partialFraction > 0) ? (4 - fullStars) : (5 - fullStars);
        }
    }
}