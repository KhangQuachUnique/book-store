package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Promotion;
import util.DBConnection;

public class PromotionDAO {

    /**
     * Lấy mã khuyến mãi theo code (nếu còn hạn).
     */
    public Promotion getPromotionByCode(String code) {
        String sql = "SELECT id, code, discount, expiry_date " +
                "FROM promotions " +
                "WHERE code = ? AND expiry_date >= NOW()";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Promotion promotion = new Promotion();
                    promotion.setId(rs.getInt("id"));
                    promotion.setCode(rs.getString("code"));
                    promotion.setDiscount(rs.getDouble("discount"));
                    promotion.setExpiryDate(rs.getTimestamp("expiry_date"));
                    return promotion;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
