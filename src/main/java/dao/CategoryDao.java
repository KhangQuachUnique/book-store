package dao;

import model.Category;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Category findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String create(Category c) {
        String sql = "INSERT INTO categories (name, parent_id, is_leaf) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            if (c.getParentId() != null) {
                ps.setLong(2, c.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setBoolean(3, c.getIsLeaf() != null ? c.getIsLeaf() : false);

            int affected = ps.executeUpdate();
            if (affected > 0) return null;
            else return "Không thêm được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String update(Category c) {
        String sql = "UPDATE categories SET name=?, parent_id=?, is_leaf=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            if (c.getParentId() != null) {
                ps.setLong(2, c.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setBoolean(3, c.getIsLeaf() != null ? c.getIsLeaf() : false);
            ps.setLong(4, c.getId());

            int affected = ps.executeUpdate();
            if (affected > 0) return null;
            else return "Không cập nhật được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String delete(Long id) {
        String sql = "DELETE FROM categories WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            if (affected > 0) return null;
            else return "Không xóa được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("parent_id") != null ? rs.getLong("parent_id") : null,
                rs.getTimestamp("created_at"),
                rs.getBoolean("is_leaf")
        );
    }

    public boolean isCategoryNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM categories WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
