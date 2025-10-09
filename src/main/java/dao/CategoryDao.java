package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import util.DBConnection;

public class CategoryDao {

    // Wrapper mở kết nối riêng (giữ tương thích)
    public List<Category> findAll() {
        try (Connection conn = DBConnection.getConnection()) {
            return findAll(conn);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Dùng một Connection được truyền vào (ưu tiên trong Servlet)
    public List<Category> findAll(Connection conn) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
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
        try (Connection conn = DBConnection.getConnection()) {
            return findById(conn, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category findById(Connection conn, Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = DBConnection.getConnection()) {
            return create(conn, c);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String create(Connection conn, Category c) {
        String sql = "INSERT INTO categories (name, parent_id, is_leaf) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            if (c.getParentId() != null) {
                ps.setLong(2, c.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setBoolean(3, c.getIsLeaf());

            int affected = ps.executeUpdate();
            if (affected > 0)
                return null;
            else
                return "Không thêm được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String update(Category c) {
        try (Connection conn = DBConnection.getConnection()) {
            return update(conn, c);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String update(Connection conn, Category c) {
        String sql = "UPDATE categories SET name=?, parent_id=?, is_leaf=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            if (c.getParentId() != null) {
                ps.setLong(2, c.getParentId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setBoolean(3, c.getIsLeaf());
            ps.setLong(4, c.getId());

            int affected = ps.executeUpdate();
            if (affected > 0)
                return null;
            else
                return "Không cập nhật được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String delete(Long id) {
        try (Connection conn = DBConnection.getConnection()) {
            return delete(conn, id);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String delete(Connection conn, Long id) {
        String sql = "DELETE FROM categories WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            if (affected > 0)
                return null;
            else
                return "Không xóa được category (không có dòng nào bị ảnh hưởng)";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setParentId(rs.getObject("parent_id") != null ? rs.getLong("parent_id") : null);
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setIsLeaf(rs.getBoolean("is_leaf"));
        return category;
    }

    public boolean isCategoryNameExists(String name) {
        try (Connection conn = DBConnection.getConnection()) {
            return isCategoryNameExists(conn, name);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isCategoryNameExists(Connection conn, String name) {
        String sql = "SELECT 1 FROM categories WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
