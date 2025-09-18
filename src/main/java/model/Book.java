package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book entity representing books table in the database.
 * Contains book information including metadata, pricing, and inventory data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private Long id;

    // Basic information
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String thumbnailUrl;

    // Classification
    private Long categoryId;

    // Physical properties
    private Integer publishYear;
    private Integer pages;

    // Inventory and pricing
    private Integer stock;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer discountRate;

    // Rating and reviews
    private BigDecimal ratingAverage;
    private Integer reviewCount;

    // Audit fields
    private Timestamp createdAt;
}
