package model.restaurant;

public class Dish {
    private int id;
    private String name;
    private double price;
    private int quantity; // Số lượng tồn kho (dùng cho đồ uống)
    private String type;  // "FOOD" hoặc "DRINK"

    public Dish() {}

    public Dish(int id, String name, double price, int quantity, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}