package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * OrderItem entity representing order_items table in the database.
 * Contains individual items within an order with quantity and pricing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    private Long id;
    private Long orderId;
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
}