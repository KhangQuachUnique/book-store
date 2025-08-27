package model;

public class Book {
    Integer id;
    String title;
    String author;
    double price;

    public Book() {
    }
    public Book(Integer id, String title, String author, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String toString() {
        return "{ \"id\": " + id + ", \"title\": \"" + title + "\", \"author\": \"" + author + "\" }";
    }
}
