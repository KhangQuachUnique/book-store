package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.OrderStatus;
import util.DBConnection;

public class OrderStatusDAO {

    /**
     * Lấy toàn bộ danh sách trạng thái đơn hàng từ bảng status
     */
    public List<OrderStatus> getAllStatuses() {
        List<OrderStatus> list = new ArrayList<>();
        list.add(new OrderStatus(0, "Tất cả"));

        String sql = "SELECT id, name FROM status ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new OrderStatus(rs.getInt("id"), rs.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
