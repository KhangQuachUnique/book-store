package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order entity representing orders table in the database. Contains order information including
 * customer, payment, and status data.
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
