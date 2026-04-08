package dao;

import model.order.Order;
import model.order.OrderDetail;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /**
     * TRANSACTION 1: TẠO ĐƠN HÀNG MỚI (ALL OR NOTHING)
     * 1. Tạo Order mới
     * 2. Lấy ID của Order vừa tạo
     * 3. Lưu danh sách OrderDetail
     * 4. Đổi trạng thái bàn thành 'OCCUPIED'
     */
    public boolean createOrderWithDetails(Order order, List<OrderDetail> details) {
        String sqlOrder = "INSERT INTO orders (customer_id, table_id, total_amount, status) VALUES (?, ?, ?, 'PENDING')";
        String sqlDetail = "INSERT INTO order_details (order_id, dish_id, quantity, price, status) VALUES (?, ?, ?, ?, 'PENDING')";
        String sqlTable = "UPDATE dining_tables SET status = 'OCCUPIED' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Bước 1: Lưu Order và lấy ID sinh tự động
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getCustomerId());
            psOrder.setInt(2, order.getTableId());
            psOrder.setDouble(3, order.getTotalAmount());
            psOrder.executeUpdate();

            ResultSet rsKeys = psOrder.getGeneratedKeys();
            int newOrderId = -1;
            if (rsKeys.next()) {
                newOrderId = rsKeys.getInt(1);
            } else {
                throw new SQLException("Không thể tạo ID cho Order.");
            }

            // Bước 2: Lưu hàng loạt (Batch) các chi tiết đơn hàng
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            for (OrderDetail detail : details) {
                psDetail.setInt(1, newOrderId);
                psDetail.setInt(2, detail.getDishId());
                psDetail.setInt(3, detail.getQuantity());
                psDetail.setDouble(4, detail.getPrice());
                psDetail.addBatch(); // Gom vào batch để chạy 1 lần cho nhanh
            }
            psDetail.executeBatch();

            // Bước 3: Cập nhật trạng thái bàn
            PreparedStatement psTable = conn.prepareStatement(sqlTable);
            psTable.setInt(1, order.getTableId());
            psTable.executeUpdate();
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Lỗi giao dịch tạo đơn hàng: " + e.getMessage());
            // NẾU CÓ BẤT KỲ LỖI GÌ -> HOÀN TÁC TOÀN BỘ (ROLLBACK)
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Đã Rollback an toàn dữ liệu Order.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Đảm bảo trả lại trạng thái mặc định và đóng kết nối
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * TRANSACTION 2: THANH TOÁN (CHECKOUT)
     * 1. Đổi trạng thái Order thành 'PAID' và gán checked_out_at
     * 2. Đổi trạng thái Bàn về 'EMPTY'
     */
    public boolean checkoutOrder(int orderId, int tableId) {
        String sqlOrder = "UPDATE orders SET status = 'PAID', checked_out_at = CURRENT_TIMESTAMP WHERE id = ?";
        String sqlTable = "UPDATE dining_tables SET status = 'EMPTY' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psOrder = conn.prepareStatement(sqlOrder);
            psOrder.setInt(1, orderId);
            psOrder.executeUpdate();

            PreparedStatement psTable = conn.prepareStatement(sqlTable);
            psTable.setInt(1, tableId);
            psTable.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Lấy danh sách các món ăn đang chờ (PENDING) hoặc đang nấu (COOKING)
    public List<OrderDetail> findPendingAndCookingDetails() {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE status IN ('PENDING', 'COOKING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setId(rs.getInt("id"));
                detail.setOrderId(rs.getInt("order_id"));
                detail.setDishId(rs.getInt("dish_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getDouble("price"));
                detail.setStatus(rs.getString("status"));
                list.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn món ăn chờ nấu: " + e.getMessage());
        }
        return list;
    }

    // Cập nhật trạng thái của riêng một chi tiết đơn hàng (1 món)
    public boolean updateDetailStatus(int detailId, String newStatus) {
        String sql = "UPDATE order_details SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, detailId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái món: " + e.getMessage());
        }
        return false;
    }
}