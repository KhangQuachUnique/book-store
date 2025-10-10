package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "\"likeReviews\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"\"reviewId\"", "\"userId\""})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"userId\"")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"reviewId\"")
    private Review review;
}
