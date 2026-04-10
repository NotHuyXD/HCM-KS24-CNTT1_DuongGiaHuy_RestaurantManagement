package presentation;

import model.people.Role;
import model.people.User;
import service.UserService;

import java.util.Scanner;

public class AuthMenu {
    private Scanner scanner;
    private UserService userService;

    public AuthMenu(Scanner scanner) {
        this.scanner = scanner;
        this.userService = new UserService();
    }

    public void start() {
        while (true) {
            System.out.println("\n=== HỆ THỐNG QUẢN LÝ NHÀ HÀNG RIKKEI ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký tài khoản mới (Dành cho Khách hàng)");
            System.out.println("0. Thoát chương trình");
            System.out.print("Mời bạn chọn chức năng: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "0":
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    private void handleLogin() {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        User loginUser = userService.login(username, password);

        if (loginUser != null) {
            System.out.println("\nĐăng nhập thành công! Chào mừng " + loginUser.getUsername());

            // ĐIỀU HƯỚNG DỰA TRÊN ROLE
            if (loginUser.getRole() == Role.MANAGER) {
                new ManagerMenu(scanner, loginUser).start();
            } else if (loginUser.getRole() == Role.CUSTOMER) {
                System.out.println("Giao diện Khách hàng đang được phát triển...");
                new CustomerMenu(scanner, loginUser).start();
            } else if (loginUser.getRole() == Role.CHEF) {
                System.out.println("Giao diện Đầu bếp đang được phát triển...");
                new ChefMenu(scanner, loginUser).start();
            }
        } else {
            System.err.println("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private void handleRegister() {
        System.out.println("\n--- ĐĂNG KÝ KHÁCH HÀNG MỚI ---");
        System.out.print("Nhập tên đăng nhập (Tối thiểu 3 ký tự): ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu (Tối thiểu 6 ký tự): ");
        String password = scanner.nextLine();

        boolean isSuccess = userService.register(username, password);
        if (isSuccess) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
        } else {
            System.err.println("Đăng ký thất bại. Vui lòng kiểm tra lại thông tin!");
        }
    }
}