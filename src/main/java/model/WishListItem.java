package model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WishListItem entity representing wishlists table in the database. Contains
 * items in user's
 * wishlist with creation timestamp.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListItem implements Serializable {

    private Long id;
    private Long userId;
    private Long bookId;

    // Audit fields
    private Timestamp createdAt;

    // Transient field for display purposes (joined from books table)
    private Book book;
}
