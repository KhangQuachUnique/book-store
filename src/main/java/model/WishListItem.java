package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "\"wishListItems\"",
        uniqueConstraints = @UniqueConstraint(columnNames = {"wishListId", "bookId"})
)
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"addedAt\"")
    private Timestamp addedAt;


    //Transient fields
    @Transient
    private Integer fullStars;

    @Transient
    private Double fractionalStars;

    @Transient
    private Integer emptyStars;


    // Relationships
    @ManyToOne
    @JoinColumn(name = "\"bookId\"", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"wishListId\"", nullable = false)
    private WishList wishList;


    // Method to calculate star representation
    public void calculateStars() {
        if (book.getAverageRating() == null) {
            fullStars = 0;
            fractionalStars = 0.0;
            emptyStars = 5;
            return;
        }

        fullStars = book.getAverageRating().intValue();
        fractionalStars = book.getAverageRating() - fullStars;
        emptyStars = 5 - fullStars - (fractionalStars > 0 ? 1 : 0);
    }
}