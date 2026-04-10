package presentation;

import model.order.OrderDetail;
import model.people.User;
import service.OrderService;

import java.util.List;
import java.util.Scanner;

public class ChefMenu {
    private Scanner scanner;
    private User currentUser;
    private OrderService orderService;

    public ChefMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.orderService = new OrderService();
    }

    public void start() {
        while (true) {
            System.out.println("\n=== KHU VỰC BẾP (CHEF) ===");
            System.out.println("Chào Bếp trưởng " + currentUser.getUsername() + "!");
            System.out.println("1. Xem danh sách các món đang chờ/đang nấu");
            System.out.println("2. Cập nhật trạng thái món ăn (Nấu xong / Bắt đầu nấu)");
            System.out.println("0. Đăng xuất");
            System.out.print("Mời sếp chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayPendingDishes();
                    break;
                case "2":
                    updateDishStatus();
                    break;
                case "0":
                    System.out.println("Đang đăng xuất...");
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayPendingDishes() {
        System.out.println("\n--- DANH SÁCH YÊU CẦU TỪ NHÀ HÀNG ---");
        List<OrderDetail> pendingList = orderService.getPendingAndCookingDetails();

        if (pendingList.isEmpty()) {
            System.out.println("Hiện tại không có món nào cần nấu. Bếp có thể nghỉ ngơi!");
            return;
        }

        System.out.printf("%-10s | %-10s | %-10s | %-10s | %-15s\n",
                "Detail ID", "Order ID", "Dish ID", "Số lượng", "Trạng thái");
        System.out.println("------------------------------------------------------------------");
        for (OrderDetail d : pendingList) {
            System.out.printf("%-10d | %-10d | %-10d | %-10d | %-15s\n",
                    d.getId(), d.getOrderId(), d.getDishId(), d.getQuantity(), d.getStatus());
        }
    }

    private void updateDishStatus() {
        System.out.println("\n--- CẬP NHẬT TIẾN ĐỘ NẤU ---");
        try {
            System.out.print("Nhập 'Detail ID' của món ăn muốn cập nhật: ");
            int detailId = Integer.parseInt(scanner.nextLine());

            System.out.println("Chọn trạng thái mới:");
            System.out.println("1. Bắt đầu nấu (COOKING)");
            System.out.println("2. Đã nấu xong, chờ bê lên (READY)");
            System.out.print("Lựa chọn của bạn: ");
            String statusChoice = scanner.nextLine();

            String newStatus = "";
            if (statusChoice.equals("1")) {
                newStatus = "COOKING";
            } else if (statusChoice.equals("2")) {
                newStatus = "READY";
            } else {
                System.err.println("Lựa chọn trạng thái không hợp lệ!");
                return;
            }

            boolean isSuccess = orderService.updateDetailStatus(detailId, newStatus);
            if (isSuccess) {
                System.out.println("Đã cập nhật trạng thái món ăn thành: " + newStatus);
            } else {
                System.err.println("Cập nhật thất bại. Vui lòng kiểm tra lại Detail ID.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số hợp lệ!");
        }
    }
}