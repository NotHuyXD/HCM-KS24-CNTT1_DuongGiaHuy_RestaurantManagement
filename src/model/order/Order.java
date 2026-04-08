package model.order;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int customerId; // FK -> users
    private int tableId;    // FK -> dining_tables
    private double totalAmount;
    private String status;  // "PENDING", "APPROVED", "PAID"
    private LocalDateTime createdAt;
    private LocalDateTime checkedOutAt;

    public Order() {}

    public Order(int id, int customerId, int tableId, double totalAmount, String status, LocalDateTime createdAt, LocalDateTime checkedOutAt) {
        this.id = id;
        this.customerId = customerId;
        this.tableId = tableId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.checkedOutAt = checkedOutAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCheckedOutAt() { return checkedOutAt; }
    public void setCheckedOutAt(LocalDateTime checkedOutAt) { this.checkedOutAt = checkedOutAt; }
}