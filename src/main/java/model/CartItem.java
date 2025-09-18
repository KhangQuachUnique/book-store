package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * CartItem entity representing carts table in the database.
 * Contains items in user's shopping cart with quantity and pricing information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer quantity;
    
    // Audit fields
    private Timestamp createdAt;
    
    // Transient fields for display purposes (joined from books table)
    private String title;
    private Double price;
}
