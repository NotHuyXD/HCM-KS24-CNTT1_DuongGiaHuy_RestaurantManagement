package service;

import dao.DishDAO;
import model.restaurant.Dish;

import java.util.List;

public class DishService {
    private DishDAO dishDAO;

    public DishService() {
        this.dishDAO = new DishDAO();
    }

    public boolean addNewDish(String name, double price, int quantity, String type) {
        // Kiểm tra tính hợp lệ của dữ liệu (Validation)
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Tên món không được để trống!");
            return false;
        }
        if (price <= 0) {
            System.err.println("Giá tiền phải lớn hơn 0!");
            return false;
        }
        if (quantity < 0) {
            System.err.println("Số lượng tồn kho không được âm!");
            return false;
        }
        if (!type.equals("FOOD") && !type.equals("DRINK")) {
            System.err.println("Loại món chỉ được là FOOD hoặc DRINK!");
            return false;
        }

        Dish newDish = new Dish(0, name, price, quantity, type);
        return dishDAO.insert(newDish);
    }

    public List<Dish> getAllDishes() {
        return dishDAO.findAll();
    }

    public boolean updateDish(int id, String name, double price, int qty, String type) {
        if (id <= 0 || price <= 0) return false;
        return dishDAO.update(new Dish(id, name, price, qty, type));
    }

    public boolean removeDish(int id) {
        return dishDAO.delete(id);
    }
}