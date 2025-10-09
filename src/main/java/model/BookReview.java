package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReview {
    private Long bookId;
    private List<ReviewShow> reviewShows = Collections.emptyList();
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
        List<Double> sourceRatings;
        if (reviewShows != null && !reviewShows.isEmpty()) {
            sourceRatings = reviewShows.stream().map(ReviewShow::getRating).toList();
        } else if (reviews != null && !reviews.isEmpty()) {
            sourceRatings = reviews.stream().map(r -> (double) r.getRating()).toList();
        } else {
            sourceRatings = Collections.emptyList();
        }

        fiveStarCount = (int) sourceRatings.stream().filter(r -> r != null && r.intValue() == 5).count();
        fourStarCount = (int) sourceRatings.stream().filter(r -> r != null && r.intValue() == 4).count();
        threeStarCount = (int) sourceRatings.stream().filter(r -> r != null && r.intValue() == 3).count();
        twoStarCount = (int) sourceRatings.stream().filter(r -> r != null && r.intValue() == 2).count();
        oneStarCount = (int) sourceRatings.stream().filter(r -> r != null && r.intValue() == 1).count();
    }
}
