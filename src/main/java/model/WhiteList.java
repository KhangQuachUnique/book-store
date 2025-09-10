package model;

import java.io.Serializable;
import java.util.List;

public class WhiteList implements Serializable {
    private List<Book> books;

    public WhiteList() {
    }

    public WhiteList(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
