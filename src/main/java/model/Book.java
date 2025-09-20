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

    public Book(Integer id, String title, String author, double price, String publisher, int categoryId, int stock,
            double originalPrice, int discount_rate, String imageUrl, String description, Integer publishYear,
            Integer pages, double rating, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.thumbnailUrl = imageUrl;
        this.description = description;
        this.publishYear = publishYear;
        this.pages = pages;
        this.rating = rating;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.stock = stock;
        this.originalPrice = originalPrice;
        this.discount_rate = discount_rate;
    }
}
