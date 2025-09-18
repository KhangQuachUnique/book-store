package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Order entity representing orders table in the database.
 * Contains order information including customer, payment, and status data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    private Long id;
    private Long userId;
    
    // Financial information
    private BigDecimal totalAmount;
    private String paymentMethod; // 'COD', 'MOMO', 'ZALOPAY', 'CARD'
    
    // Status and promotions
    private Long statusId;
    private Long promotionId;
    
    // Audit fields
    private Timestamp createdAt;
    private Timestamp updatedAt;
}