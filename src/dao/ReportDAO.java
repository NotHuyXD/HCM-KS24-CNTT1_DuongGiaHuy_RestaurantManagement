package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    // 1. Thống kê tổng doanh thu theo Tháng và Năm (Dựa vào các hóa đơn đã PAID)
    public double getRevenueByMonthAndYear(int month, int year) {
        String sql = "SELECT SUM(total_amount) AS revenue FROM orders " +
                "WHERE status = 'PAID' AND MONTH(checked_out_at) = ? AND YEAR(checked_out_at) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("revenue");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê doanh thu: " + e.getMessage());
        }
        return 0;
    }

    // 2. Thống kê món ăn bán chạy nhất (Top N món)
    public void printTopSellingDishes(int limit) {
        String sql = "SELECT d.name, SUM(od.quantity) AS total_sold " +
                "FROM order_details od " +
                "JOIN dishes d ON od.dish_id = d.id " +
                "WHERE od.status = 'SERVED' " +
                "GROUP BY d.id, d.name " +
                "ORDER BY total_sold DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            System.out.printf("%-20s | %-10s\n", "Tên Món Ăn", "Đã Bán");
            System.out.println("-----------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-10d\n", rs.getString("name"), rs.getInt("total_sold"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê món ăn: " + e.getMessage());
        }
    }
}