package dao;

import model.OrderStatus;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDAO {

    /**
     * Lấy toàn bộ danh sách trạng thái đơn hàng từ bảng status
     */
    public List<OrderStatus> getAllStatuses() {
        List<OrderStatus> list = new ArrayList<>();
        list.add(new OrderStatus(0, "Tất cả"));

        String sql = "SELECT id, name FROM status WHERE id != 8 ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new OrderStatus(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
