package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
    private String thumbnailUrl;
    private String description;
    private Integer stock;
    private BigDecimal ratingAverage;
    private Integer reviewCount;
    private Integer publishYear;
    private Integer pages;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer discountRate;
    private Timestamp createdAt;
}
