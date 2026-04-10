package dao;

import model.restaurant.DiningTable;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiningTableDAO {

    // Lấy danh sách toàn bộ bàn ăn
    public List<DiningTable> findAll() {
        List<DiningTable> list = new ArrayList<>();
        String sql = "SELECT * FROM dining_tables";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DiningTable table = new DiningTable();
                table.setId(rs.getInt("id"));
                table.setTableName(rs.getString("table_name"));
                table.setStatus(rs.getString("status"));
                list.add(table);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách bàn: " + e.getMessage());
        }
        return list;
    }

    // Cập nhật trạng thái bàn (Khi khách vào ngồi hoặc thanh toán xong)
    public boolean updateStatus(int tableId, String newStatus) {
        String sql = "UPDATE dining_tables SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, tableId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái bàn: " + e.getMessage());
        }
        return false;
    }

    public boolean insert(DiningTable table) {
        String sql = "INSERT INTO dining_tables (table_name, status) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table.getTableName());
            ps.setString(2, "EMPTY"); // Mặc định bàn mới là trống
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm bàn: " + e.getMessage());
            return false;
        }
    }

    public boolean update(DiningTable table) {
        String sql = "UPDATE dining_tables SET table_name = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table.getTableName());
            ps.setInt(2, table.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi sửa tên bàn: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM dining_tables WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa bàn: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra ID bàn có tồn tại hay ko
    public DiningTable findById(int id) {
        String sql = "SELECT * FROM dining_tables WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DiningTable table = new DiningTable();
                    table.setId(rs.getInt("id"));
                    table.setTableName(rs.getString("table_name"));
                    table.setStatus(rs.getString("status"));
                    return table;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra bàn: " + e.getMessage());
        }
        return null;
    }
}