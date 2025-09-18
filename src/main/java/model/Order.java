package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private Long statusId;
    private String paymentMethod; // 'COD', 'MOMO', 'ZALOPAY', 'CARD'
    private Long promotionId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}