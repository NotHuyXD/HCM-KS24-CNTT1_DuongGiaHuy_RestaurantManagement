package presentation;

import model.order.OrderDetail;
import model.people.User;
import model.restaurant.DiningTable;
import model.restaurant.Dish;
import service.DiningTableService;
import service.DishService;
import service.OrderService;
import service.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private Scanner scanner;
    private User currentUser;
    private DishService dishService;
    private OrderService orderService;
    private ReviewService reviewService;
    private DiningTableService tableService;

    public CustomerMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.dishService = new DishService();
        this.orderService = new OrderService();
        this.reviewService = new ReviewService();
        this.tableService = new DiningTableService();
    }

    public void start() {
        while (true) {
            System.out.println("\n=== DÀNH CHO KHÁCH HÀNG ===");
            System.out.println("Xin chào, " + currentUser.getUsername() + "!");
            System.out.println("1. Xem thực đơn");
            System.out.println("2. Đặt món (Tạo Order)");
            System.out.println("3. Viết đánh giá (Review)");
            System.out.println("4. Xem tất cả đánh giá của nhà hàng");
            System.out.println("0. Đăng xuất");
            System.out.print("Mời bạn chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayMenu();
                    break;
                case "2":
                    orderFood();
                    break;
                case "3":
                    leaveReview();
                    break;
                case "4":
                    viewReviews();
                    break;
                case "0":
                    System.out.println("Đang đăng xuất...");
                    return;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- THỰC ĐƠN NHÀ HÀNG ---");
        List<Dish> dishes = dishService.getAllDishes();
        if (dishes.isEmpty()) {
            System.out.println("Thực đơn đang cập nhật!");
            return;
        }
        System.out.printf("%-5s | %-20s | %-10s\n", "ID", "Tên món", "Giá ($)");
        System.out.println("----------------------------------------");
        for (Dish d : dishes) {
            System.out.printf("%-5d | %-20s | %-10.2f\n", d.getId(), d.getName(), d.getPrice());
        }
    }

    private void orderFood() {
        System.out.println("\n--- ĐẶT MÓN ---");
        try {
            System.out.print("Bạn đang ngồi ở Bàn số mấy? (Nhập ID Bàn): ");
            int tableId = Integer.parseInt(scanner.nextLine());

            // KIỂM TRA BÀN CÓ TỒN TẠI KHÔNG
            DiningTable table = tableService.getTableById(tableId);
            if (table == null) {
                System.err.println("Lỗi: Bàn số " + tableId + " không tồn tại trong nhà hàng!");
                return;
            }

            if (table.getStatus().equals("OCCUPIED")) {
                System.out.println("Lưu ý: Bàn này hiện đang được đánh dấu là có khách.");
                System.out.println("Tiếp tục gọi thêm món cho bàn này...");
            } else {
                System.out.println("Xác nhận: Bạn đang gọi món cho " + table.getTableName());
            }

            List<OrderDetail> cart = new ArrayList<>();
            while (true) {
                System.out.print("Nhập ID món ăn muốn gọi (Nhập 0 để chốt đơn): ");
                int dishId = Integer.parseInt(scanner.nextLine());

                if (dishId == 0) break;

                System.out.print("Nhập số lượng: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                double currentPrice = 10.0;

                OrderDetail detail = new OrderDetail();
                detail.setDishId(dishId);
                detail.setQuantity(quantity);
                detail.setPrice(currentPrice);
                cart.add(detail);
                System.out.println("Đã thêm vào giỏ!");
            }

            if (!cart.isEmpty()) {
                boolean success = orderService.placeOrder(currentUser.getId(), tableId, cart);
                if (success) {
                    System.out.println("Đặt món thành công! Vui lòng đợi đầu bếp chuẩn bị.");
                } else {
                    System.err.println("Có lỗi xảy ra khi đặt món.");
                }
            } else {
                System.out.println("Bạn chưa chọn món nào.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số hợp lệ!");
        }
    }

    private void leaveReview() {
        System.out.println("\n--- ĐÁNH GIÁ DỊCH VỤ ---");
        try {
            System.out.print("Bạn muốn đánh giá món ăn nào? (Nhập ID món, hoặc bỏ trống nếu đánh giá chung): ");
            String dishInput = scanner.nextLine();
            Integer dishId = dishInput.isEmpty() ? null : Integer.parseInt(dishInput);

            System.out.print("Chấm điểm (1-5 sao): ");
            int rating = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập bình luận của bạn: ");
            String comment = scanner.nextLine();

            boolean success = reviewService.addReview(currentUser.getId(), dishId, rating, comment);
            if (success) {
                System.out.println("Cảm ơn bạn đã đánh giá!");
            } else {
                System.err.println("Gửi đánh giá thất bại.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập đúng định dạng số!");
        }
    }

    // --- XEM ĐÁNH GIÁ ---
    private void viewReviews() {
        System.out.println("\n--- NHỮNG LỜI NHẬN XÉT TỪ THỰC KHÁCH ---");
        System.out.println("1. Xem tất cả đánh giá");
        System.out.println("2. Xem đánh giá theo món ăn cụ thể");
        System.out.print("Lựa chọn của bạn: ");
        String choice = scanner.nextLine();

        java.util.List<model.feedback.Review> reviews = new java.util.ArrayList<>();

        if (choice.equals("1")) {
            reviews = reviewService.getAllReviews();
        } else if (choice.equals("2")) {
            System.out.print("Nhập ID món ăn bạn muốn xem đánh giá: ");
            try {
                int dishId = Integer.parseInt(scanner.nextLine());
                reviews = reviewService.getReviewsByDish(dishId);
            } catch (NumberFormatException e) {
                System.err.println("ID món ăn phải là một số nguyên!");
                return;
            }
        } else {
            System.err.println("Lựa chọn không hợp lệ!");
            return;
        }

        if (reviews.isEmpty()) {
            System.out.println("Chưa có đánh giá nào ở mục này.");
            return;
        }

        System.out.printf("%-5s | %-10s | %-5s | %-40s | %-20s\n", "ID", "Món (ID)", "Sao", "Bình luận", "Ngày đăng");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (model.feedback.Review r : reviews) {
            String dishInfo = (r.getDishId() == null) ? "Chung" : String.valueOf(r.getDishId());

            String time = r.getCreatedAt().toString().substring(0, 16).replace("T", " ");

            System.out.printf("%-5d | %-10s | %-5d | %-40s | %-20s\n",
                    r.getId(), dishInfo, r.getRating(), r.getComment(), time);
        }
    }
}