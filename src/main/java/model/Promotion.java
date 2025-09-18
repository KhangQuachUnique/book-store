package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    private Long id;
    private String code;
    private BigDecimal discount;
    private Timestamp expiryDate;
    private Timestamp createdAt;
}