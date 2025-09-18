package dao;

import model.CartItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    public void addToCart(int userId, int bookId, int quantity) throws Exception {
        String sql = "INSERT INTO carts(user_id, book_id, quantity) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, book_id) DO UPDATE SET quantity = carts.quantity + EXCLUDED.quantity";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public List<CartItem> getCartByUser(int userId) throws Exception {
        String sql = "SELECT c.id, c.book_id, c.quantity, b.title, b.price " +
                "FROM carts c JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";
        List<CartItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem ci = new CartItem();
                ci.setId(rs.getLong("id"));
                ci.setBookId(rs.getLong("book_id"));
                ci.setQuantity(rs.getInt("quantity"));
                ci.setTitle(rs.getString("title"));
                ci.setPrice(rs.getDouble("price"));
                list.add(ci);
            }
        }
        return list;
    }

    public void removeFromCart(int cartId) throws Exception {
        String sql = "DELETE FROM carts WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int userId) throws Exception {
        String sql = "DELETE FROM carts WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
