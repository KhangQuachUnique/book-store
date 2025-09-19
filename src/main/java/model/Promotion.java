package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Promotion entity representing promotions table in the database. Contains
 * discount codes and
 * promotional offers with expiry dates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    private Long id;
    private String code;
    private BigDecimal discount; // Percentage (0-100)
    private Timestamp expiryDate;

    // Audit fields
    private Timestamp createdAt;
}
