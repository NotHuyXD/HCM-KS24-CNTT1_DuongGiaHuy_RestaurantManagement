package dao;

import model.people.Role;
import model.people.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 1. Chức năng Đăng nhập
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password); // Thực tế nên mã hóa mật khẩu, nhưng ở mức cơ bản ta so sánh chuỗi

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Đọc dữ liệu từ DB và map sang Object User
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                // Chuyển đổi chuỗi String từ DB sang kiểu Enum Role
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setActive(rs.getBoolean("is_active"));

                return user;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đăng nhập: " + e.getMessage());
        }
        return null; // Trả về null nếu sai tài khoản/mật khẩu
    }

    // 2. Kiểm tra tên đăng nhập đã tồn tại chưa (Dùng cho Đăng ký)
    public boolean isUsernameExist(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu có kết quả (đã tồn tại)

        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra username: " + e.getMessage());
        }
        return false;
    }

    // 3. Chức năng Đăng ký tài khoản mới (Mặc định cho Customer)
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, role, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name()); // Lưu Enum dưới dạng String
            ps.setBoolean(4, user.isActive());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi đăng ký: " + e.getMessage());
        }
        return false;
    }

    // 4. Lấy danh sách toàn bộ người dùng (Dành cho chức năng của Manager)
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setActive(rs.getBoolean("is_active"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách user: " + e.getMessage());
        }
        return list;
    }

    // 5. Cập nhật trạng thái (Khóa / Mở khóa tài khoản - Dành cho Manager)
    public boolean changeStatus(int userId, boolean newStatus) {
        String sql = "UPDATE users SET is_active = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, newStatus);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái user: " + e.getMessage());
        }
        return false;
    }
}