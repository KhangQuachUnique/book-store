package dao;

import model.Address;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, address.getUserId());
            pstmt.setString(2, address.getAddress());
            pstmt.setBoolean(3, address.isDefaultAddress());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    address.setId(rs.getLong(1));
                }
            }
        }
    }

    // CHANGE: Thêm updateAddress để hỗ trợ inline edit từ viewUser
    public void updateAddress(Address address) throws SQLException {
        String query = "UPDATE addresses SET address = ?, is_default = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, address.getAddress());
            pstmt.setBoolean(2, address.isDefaultAddress());
            pstmt.setLong(3, address.getId());
            pstmt.setLong(4, address.getUserId());
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Address ID " + address.getId() + " not found or does not belong to user " + address.getUserId());
            }
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

    public void deleteAddress(long addressId, long userId) throws SQLException {
        String query = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, addressId);
            pstmt.setLong(2, userId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Address ID " + addressId + " not found or does not belong to user " + userId);
            }
        }
    }

    private Address extractAddressFromResultSet(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getLong("id"));
        address.setUserId(rs.getLong("user_id"));
        address.setAddress(rs.getString("address"));
        address.setDefaultAddress(rs.getBoolean("is_default"));
        address.setCreatedAt(rs.getTimestamp("created_at"));
        return address;
    }
}