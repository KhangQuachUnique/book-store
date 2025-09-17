package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewShow {
    private Integer id;
    private Integer userId;
    private String username;
    private String avatarUrl;
    private Double rating;
    private Integer fullStars;
    private Double partialFraction;
    private Integer emptyStars;
    private String comment;
    private Integer likeCount;
    private Timestamp date;
    private Boolean likedByCurrentUser;

    public void calculateStars() {
        // This method can be used to calculate star representation if needed
        this.fullStars = rating.intValue();
        this.partialFraction = rating - fullStars;
        this.emptyStars = (partialFraction > 0) ? (4 - fullStars) : (5 - fullStars);
    }
}