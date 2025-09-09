package model;

import java.io.Serializable;

public class Book implements Serializable {
    private Integer id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private String genre;
    private String imageUrl;
    private String description;
    private Integer publishYear;
    private Integer pages;
    private double rating;
    private double price;
    private String createdAt;

    public Book() {
    }

    public Book(Integer id, String title, String author, double price, String isbn, String publisher, String genre, String imageUrl, String description, Integer publishYear, Integer pages, double rating, String createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.publisher = publisher;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.description = description;
        this.publishYear = publishYear;
        this.pages = pages;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "{ \"id\": " + id + ", \"title\": \"" + title + "\", \"author\": \"" + author + "\" , \"price\": " + price + ", \"isbn\": \"" + isbn + "\", \"publisher\": \"" + publisher + "\", \"genre\": \"" + genre + "\", \"imageUrl\": \"" + imageUrl + "\", \"description\": \"" + description + "\", \"publishYear\": " + publishYear + ", \"pages\": " + pages + ", \"rating\": " + rating + ", \"createdAt\": \"" + createdAt + "\" }";
    }
}
