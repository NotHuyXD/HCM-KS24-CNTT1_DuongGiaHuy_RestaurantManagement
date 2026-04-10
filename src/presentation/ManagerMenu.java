package presentation;

import model.people.Role;
import model.people.User;
import model.restaurant.DiningTable;
import service.DiningTableService;
import service.DishService;
import service.ReportService;
import service.UserService;
import java.util.Scanner;

public class ManagerMenu {
    private Scanner scanner;
    private DishService dishService = new DishService();
    private DiningTableService tableService = new DiningTableService();
    private UserService userService = new UserService();
    private ReportService reportService = new ReportService();

    public ManagerMenu(Scanner scanner, User user) { this.scanner = scanner; }

    public void start() {
        while (true) {
            System.out.println("\n=== MENU QUẢN LÝ (NÂNG CAO) ===");
            System.out.println("1. Quản lý thực đơn (Món ăn/Đồ uống)");
            System.out.println("2. Quản lý bàn ăn");
            System.out.println("3. Quản lý người dùng (Ban/Tạo tài khoản)");
            System.out.println("4. Thống kê & Báo cáo");
            System.out.println("0. Đăng xuất");
            System.out.print("Lựa chọn: ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) manageDishes();
            else if (choice.equals("2")) manageTables();
            else if (choice.equals("3")) manageUsers();
            else if (choice.equals("4")) viewReports();
            else if (choice.equals("0")) break;
        }
    }

    // --- QUẢN LÝ NGƯỜI DÙNG ---
    private void manageUsers() {
        System.out.println("\n1. Xem danh sách User | 2. Tạo tài khoản Đầu Bếp | 3. Khóa/Mở khóa User | 0. Quay lại");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.printf("%-5s | %-15s | %-10s | %-10s\n", "ID", "Username", "Role", "Trạng thái");
                for (User u : userService.getAllUsers()) {
                    String status = u.isActive() ? "Hoạt động" : "BỊ KHÓA";
                    System.out.printf("%-5d | %-15s | %-10s | %-10s\n", u.getId(), u.getUsername(), u.getRole(), status);
                }
                break;
            case "2":
                System.out.print("Nhập Username Đầu bếp mới: "); String chefName = scanner.nextLine();
                System.out.print("Nhập Mật khẩu: "); String chefPass = scanner.nextLine();
                boolean isSuccess=userService.createChef(chefName,chefPass);
                if(isSuccess){
                    System.out.println("Đăng ký tài khoản đầu bếp thành công!");
                } else {
                    System.err.println("Đăng ký thất bại. Vui lòng kiểm tra lại thông tin!");
                }
                break;
            case "3":
                System.out.print("Nhập ID User muốn đổi trạng thái: ");
                int uid = Integer.parseInt(scanner.nextLine());
                System.out.print("Nhập trạng thái (true = Mở khóa, false = Khóa): ");
                boolean stt = Boolean.parseBoolean(scanner.nextLine());
                if (userService.changeUserStatus(uid, stt)) System.out.println("Đã cập nhật trạng thái!");
                break;
        }
    }

    // --- THỐNG KÊ ---
    private void viewReports() {
        System.out.println("\n1. Doanh thu theo tháng | 2. Top món bán chạy | 0. Quay lại");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Nhập tháng (1-12): "); int m = Integer.parseInt(scanner.nextLine());
                System.out.print("Nhập năm (vd: 2024): "); int y = Integer.parseInt(scanner.nextLine());
                reportService.showMonthlyRevenue(m, y);
                break;
            case "2":
                System.out.print("Bạn muốn xem Top mấy món? (vd: 3, 5): ");
                int top = Integer.parseInt(scanner.nextLine());
                reportService.showTopSellingDishes(top);
                break;
        }
    }

    // --- QUẢN LÝ MÓN ĂN (Sửa, Xóa) ---
    private void manageDishes() {
        System.out.println("\n1. Xem thực đơn | 2. Sửa món | 3. Xóa món | 4. Thêm món | 0. Quay lại");
        String choice = scanner.nextLine();
        switch (choice) {
            case "2":
                System.out.print("Nhập ID món muốn sửa: ");
                int editId = Integer.parseInt(scanner.nextLine());
                System.out.print("Tên mới: "); String name = scanner.nextLine();
                System.out.print("Giá mới: "); double price = Double.parseDouble(scanner.nextLine());
                System.out.print("Loại (FOOD/DRINK): "); String type = scanner.nextLine().toUpperCase();
                int qty = type.equals("DRINK") ? Integer.parseInt(scanner.nextLine()) : 0;
                if (dishService.updateDish(editId, name, price, qty, type)) System.out.println("Đã cập nhật!");
                break;
            case "3":
                System.out.print("Nhập ID món muốn xóa: ");
                int delId = Integer.parseInt(scanner.nextLine());
                if (dishService.removeDish(delId)) System.out.println("Đã xóa!");
                break;
        }
    }

    // --- QUẢN LÝ BÀN ĂN (CRUD) ---
    private void manageTables() {
        System.out.println("\n1. Danh sách bàn | 2. Thêm bàn | 3. Sửa bàn | 4. Xóa bàn | 0. Quay lại");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                for (DiningTable t : tableService.getAllTables())
                    System.out.println("ID: " + t.getId() + " | Tên: " + t.getTableName() + " | Trạng thái: " + t.getStatus());
                break;
            case "2":
                System.out.print("Tên bàn mới: ");
                if (tableService.addTable(scanner.nextLine())) System.out.println("Đã thêm bàn!");
                break;
            case "3":
                System.out.print("ID bàn cần sửa: "); int id = Integer.parseInt(scanner.nextLine());
                System.out.print("Tên mới: ");
                if (tableService.updateTableName(id, scanner.nextLine())) System.out.println("Đã sửa!");
                break;
            case "4":
                System.out.print("ID bàn cần xóa: ");
                if (tableService.deleteTable(Integer.parseInt(scanner.nextLine()))) System.out.println("Đã xóa!");
                break;
        }
    }
}