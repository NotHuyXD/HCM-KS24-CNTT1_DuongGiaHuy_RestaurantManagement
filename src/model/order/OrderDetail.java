package model.order;

public class OrderDetail {
    private int id;
    private int orderId; // FK -> orders
    private int dishId;  // FK -> dishes
    private int quantity;
    private double price; // Giá lưu tại thời điểm gọi (tránh việc sau này đổi giá menu làm sai hóa đơn cũ)
    private String status; // "PENDING", "COOKING", "READY", "SERVED"

    public OrderDetail() {}

    public OrderDetail(int id, int orderId, int dishId, int quantity, double price, String status) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getDishId() { return dishId; }
    public void setDishId(int dishId) { this.dishId = dishId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}