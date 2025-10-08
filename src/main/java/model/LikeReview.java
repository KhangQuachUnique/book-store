package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"likeReviews\"")
public class LikeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"userId\"", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "\"bookId\"", insertable = false, updatable = false)
    private Long bookId;

    @Column(name = "\"reviewId\"", insertable = false, updatable = false)
    private Long reviewId;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"bookId\"", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"reviewId\"", nullable = false)
    private Review review;
}