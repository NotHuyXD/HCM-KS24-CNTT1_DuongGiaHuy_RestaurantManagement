package service;

import dao.OrderDAO;
import model.order.Order;
import model.order.OrderDetail;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    // Nghiệp vụ Tạo đơn hàng mới
    public boolean placeOrder(int customerId, int tableId, List<OrderDetail> details) {
        if (details == null || details.isEmpty()) {
            System.err.println("Đơn hàng phải có ít nhất 1 món!");
            return false;
        }

        double totalAmount = 0;
        for (OrderDetail detail : details) {
            if (detail.getQuantity() <= 0) {
                System.err.println("Số lượng món ăn phải lớn hơn 0!");
                return false;
            }
            totalAmount += (detail.getPrice() * detail.getQuantity());
        }

        Order newOrder = new Order();
        newOrder.setCustomerId(customerId);
        newOrder.setTableId(tableId);
        newOrder.setTotalAmount(totalAmount);

        return orderDAO.createOrderWithDetails(newOrder, details);
    }

    // Thanh toán
    public boolean checkout(int orderId, int tableId) {
        if (orderId <= 0 || tableId <= 0) {
            System.err.println("Thông tin thanh toán không hợp lệ!");
            return false;
        }
        return orderDAO.checkoutOrder(orderId, tableId);
    }

    // Trả về danh sách món cho bếp
    public List<OrderDetail> getPendingAndCookingDetails() {
        return orderDAO.findPendingAndCookingDetails();
    }

    // Cập nhật trạng thái món ăn
    public boolean updateDetailStatus(int detailId, String newStatus) {
        if (detailId <= 0) {
            System.err.println("ID chi tiết đơn hàng không hợp lệ!");
            return false;
        }
        if (!newStatus.equals("PENDING") && !newStatus.equals("COOKING") &&
                !newStatus.equals("READY") && !newStatus.equals("SERVED")) {
            System.err.println("Trạng thái không hợp lệ!");
            return false;
        }
        return orderDAO.updateDetailStatus(detailId, newStatus);
    }
}