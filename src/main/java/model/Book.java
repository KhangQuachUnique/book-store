package model;

import jakarta.validation.constraints.*; // ✅ Đổi từ javax → jakarta
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing a book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private Integer id;

    @NotNull(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    private String author;
    private String publisher;

    @Min(value = 1, message = "Category ID must be positive")
    private int categoryId;

    private String thumbnailUrl;
    private String description;

    @Min(value = 0, message = "Stock must be non-negative")
    private int stock;

    private Integer publishYear;

    @Min(value = 1, message = "Pages must be positive")
    private Integer pages;

    @Min(value = 0, message = "Rating must be non-negative")
    @Max(value = 5, message = "Rating must not exceed 5")
    private double rating;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @Min(value = 0, message = "Original price must be non-negative")
    private double originalPrice;

    @Min(value = 0, message = "Discount rate must be non-negative")
    @Max(value = 100, message = "Discount rate must not exceed 100")
    private int discount_rate;

    private Timestamp createdAt;
}
