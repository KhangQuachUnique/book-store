package model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReviewShow {
    private Integer id;
    private Integer userId;
    private Double rating;
    private String username;
    private String comment;
    private Integer likeCount;
    private String avatarUrl;
    private Timestamp date;
    private Boolean likedByCurrentUser;

    private Integer fullStars;
    private Double fractionalStars;
    private Integer emptyStars;

    public void calculateStars() {
        if (rating == null) {
            fullStars = 0;
            fractionalStars = 0.0;
            emptyStars = 5;
            return;
        }
        fullStars = rating.intValue();
        fractionalStars = rating - fullStars;
        emptyStars = 5 - fullStars - (fractionalStars > 0 ? 1 : 0);
    }
}
