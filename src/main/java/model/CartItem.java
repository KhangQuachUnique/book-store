package model;

import lombok.Data;

@Data
public class CartItem {
    private int id;
    private int userId;
    private int bookId;
    private int quantity;
    private String thumbnail;
    private String title;
    private double price;
}
