package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import util.DBConnection;

public class AddressDao {

    public List<Address> getAddressesByUserId(long userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(extractAddressFromResultSet(rs));
                }
            }
        }
        return addresses;
    }

    public void createAddress(Address address) throws SQLException {
        String query = "INSERT INTO addresses (user_id, address, is_default) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, address.getUserId());
            pstmt.setString(2, address.getAddress());
            pstmt.setBoolean(3, address.getIsDefaultAddress());
            pstmt.executeUpdate();
        }
    }

    public void setDefaultAddress(long addressId, long userId) throws SQLException {
        String resetQuery = "UPDATE addresses SET is_default = false WHERE user_id = ?";
        String setQuery = "UPDATE addresses SET is_default = true WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            // Reset all addresses to non-default
            try (PreparedStatement resetPstmt = conn.prepareStatement(resetQuery)) {
                resetPstmt.setLong(1, userId);
                resetPstmt.executeUpdate();
            }
            // Set selected address as default
            try (PreparedStatement setPstmt = conn.prepareStatement(setQuery)) {
                setPstmt.setLong(1, addressId);
                setPstmt.setLong(2, userId);
                setPstmt.executeUpdate();
            }
        }
    }

    private Address extractAddressFromResultSet(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getLong("id"));
        address.setUserId(rs.getLong("user_id"));
        address.setAddress(rs.getString("address"));
        address.setIsDefaultAddress(rs.getBoolean("is_default"));
        address.setCreatedAt(rs.getTimestamp("created_at"));
        return address;
    }

    public void deleteAddress(long addressId, long userId) throws SQLException {
        String query = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, addressId);
            pstmt.setLong(2, userId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Address ID " + addressId
                        + " not found or does not belong to user " + userId);
            }
        }
    }
}
