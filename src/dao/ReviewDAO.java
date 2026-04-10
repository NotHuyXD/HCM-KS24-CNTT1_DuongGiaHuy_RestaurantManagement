package dao;

import model.feedback.Review;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // 1. Khách hàng viết đánh giá mới
    public boolean insert(Review review) {
        String sql = "INSERT INTO reviews (customer_id, dish_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getCustomerId());

            if (review.getDishId() != null) {
                ps.setInt(2, review.getDishId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi thêm đánh giá: " + e.getMessage());
        }
        return false;
    }

    // 2. Lấy toàn bộ đánh giá
    public List<Review> findAll() {
        List<Review> list = new ArrayList<>();
        // Sắp xếp giảm dần theo thời gian
        String sql = "SELECT * FROM reviews ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("id"));
                review.setCustomerId(rs.getInt("customer_id"));

                Object dishIdObj = rs.getObject("dish_id");
                if (dishIdObj != null) {
                    review.setDishId((Integer) dishIdObj);
                } else {
                    review.setDishId(null);
                }

                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(review);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách đánh giá: " + e.getMessage());
        }
        return list;
    }

    // 3. Lấy đánh giá của một món ăn cụ thể (Để hiển thị khi xem chi tiết món)
    public List<Review> findByDishId(int dishId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE dish_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setCustomerId(rs.getInt("customer_id"));
                    review.setDishId(rs.getInt("dish_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setComment(rs.getString("comment"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    list.add(review);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy đánh giá theo món: " + e.getMessage());
        }
        return list;
    }
}