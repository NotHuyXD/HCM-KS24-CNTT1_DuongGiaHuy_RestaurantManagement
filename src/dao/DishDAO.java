package dao;

import model.restaurant.Dish;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {

    // Thêm món mới vào thực đơn
    public boolean insert(Dish dish) {
        String sql = "INSERT INTO dishes (name, price, quantity, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            ps.setInt(3, dish.getQuantity());
            ps.setString(4, dish.getType());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm món ăn: " + e.getMessage());
        }
        return false;
    }

    // Lấy toàn bộ thực đơn
    public List<Dish> findAll() {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dishes";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dish dish = new Dish(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("type")
                );
                list.add(dish);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy thực đơn: " + e.getMessage());
        }
        return list;
    }

    public boolean update(Dish dish) {
        String sql = "UPDATE dishes SET name = ?, price = ?, quantity = ?, type = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            ps.setInt(3, dish.getQuantity());
            ps.setString(4, dish.getType());
            ps.setInt(5, dish.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi sửa món ăn: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM dishes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa món ăn: " + e.getMessage());
            return false;
        }
    }
}