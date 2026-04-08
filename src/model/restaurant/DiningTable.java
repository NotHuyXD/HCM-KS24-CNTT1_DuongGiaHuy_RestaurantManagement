package model.restaurant;

public class DiningTable {
    private int id;
    private String tableName;
    private String status; // "EMPTY", "OCCUPIED"

    public DiningTable() {}

    public DiningTable(int id, String tableName, String status) {
        this.id = id;
        this.tableName = tableName;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}