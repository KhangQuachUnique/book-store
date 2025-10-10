package model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReview {
    private List<Review> reviews;
    private Double averageRating;
    private Integer totalReviews;

    private Integer fullStars;
    private Double fractionalStars;
    private Integer emptyStars;

    private Integer fiveStarCount;
    private Integer fourStarCount;
    private Integer threeStarCount;
    private Integer twoStarCount;
    private Integer oneStarCount;

    // Method to calculate star representation
    public void calculateStars() {
        if (averageRating == null) {
            fullStars = 0;
            fractionalStars = 0.0;
            emptyStars = 5;
            return;
        }

        fullStars = averageRating.intValue();
        fractionalStars = averageRating - fullStars;
        emptyStars = 5 - fullStars - (fractionalStars > 0 ? 1 : 0);
    }

    public void calculateStarCounts() {
        fiveStarCount = (int) reviews.stream().filter(r -> r.getRating() == 5).count();
        fourStarCount = (int) reviews.stream().filter(r -> r.getRating() == 4).count();
        threeStarCount = (int) reviews.stream().filter(r -> r.getRating() == 3).count();
        twoStarCount = (int) reviews.stream().filter(r -> r.getRating() == 2).count();
        oneStarCount = (int) reviews.stream().filter(r -> r.getRating() == 1).count();
    }
}
