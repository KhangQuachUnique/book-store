package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private int categoryId;
    private String thumbnailUrl;
    private String description;
    private int stock;
    private Integer publishYear;
    private Integer pages;
    private double rating;
    private double price;
    private double originalPrice;
    private int discount_rate;
    private Timestamp createdAt;
}
