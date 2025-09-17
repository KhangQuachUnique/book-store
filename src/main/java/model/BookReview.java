package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookReview {
    private Integer id;
    private Integer bookId;
    private List<ReviewShow> reviewShows;
    private Double averageRating;
    private Integer totalReviews;
    private Integer fullStars;
    private Double partialFraction;
    private Integer emptyStars;
    private Integer fiveStarCount;
    private Integer fourStarCount;
    private Integer threeStarCount;
    private Integer twoStarCount;
    private Integer oneStarCount;

    public void calculateStars() {
        // This method can be used to calculate star representation if needed
        this.fullStars = averageRating.intValue();
        this.partialFraction = averageRating - fullStars;
        this.emptyStars = (partialFraction > 0) ? (4 - fullStars) : (5 - fullStars);
    }
}
