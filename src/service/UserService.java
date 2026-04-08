package service;

import dao.UserDAO;
import model.people.Role;
import model.people.User;

import java.util.List;

public class UserService {
    // Service sẽ gọi đến DAO
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // 1. Nghiệp vụ Đăng nhập
    public User login(String username, String password) {
        // Kiểm tra validation cơ bản trước khi chọc xuống DB
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Tên đăng nhập không được để trống!");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Mật khẩu không được để trống!");
            return null;
        }

        User user = userDAO.login(username, password);

        // Kiểm tra xem tài khoản có bị quản lý khóa không
        if (user != null && !user.isActive()) {
            System.err.println("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Quản lý!");
            return null;
        }

        return user;
    }

    // 2. Nghiệp vụ Đăng ký tài khoản (Mặc định là Khách hàng)
    public boolean register(String username, String password) {
        if (username == null || username.trim().length() < 3) {
            System.err.println("Tên đăng nhập phải có ít nhất 3 ký tự!");
            return false;
        }
        if (password == null || password.trim().length() < 6) {
            System.err.println("Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }

        // Kiểm tra trùng lặp username bằng cách gọi DAO
        if (userDAO.isUsernameExist(username)) {
            System.err.println("Tên đăng nhập này đã tồn tại. Vui lòng chọn tên khác!");
            return false;
        }

        // Tạo đối tượng User mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(Role.CUSTOMER); // Đăng ký mới mặc định là Khách hàng
        newUser.setActive(true);        // Mặc định kích hoạt

        return userDAO.register(newUser);
    }

    // 3. Nghiệp vụ lấy danh sách người dùng (Dành cho Manager)
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // 4. Nghiệp vụ Khóa/Mở khóa tài khoản
    public boolean changeUserStatus(int userId, boolean newStatus) {
        if (userId <= 0) {
            System.err.println("ID người dùng không hợp lệ!");
            return false;
        }
        return userDAO.changeStatus(userId, newStatus);
    }

    public boolean createChef(String username, String password) {
        if (username == null || username.trim().length() < 3) {
            System.err.println("Tên đăng nhập phải có ít nhất 3 ký tự!");
            return false;
        }
        if (password == null || password.trim().length() < 6) {
            System.err.println("Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }
        if (userDAO.isUsernameExist(username)) {
            System.err.println("Tên đăng nhập này đã tồn tại. Vui lòng chọn tên khác!");
            return false;
        }
        User newChef = new User();
        newChef.setUsername(username);
        newChef.setPassword(password);
        newChef.setRole(Role.CHEF);
        newChef.setActive(true);

        return userDAO.register(newChef);
    }
}