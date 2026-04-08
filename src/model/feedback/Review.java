package model.feedback;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int customerId; // FK -> users
    private Integer dishId; // FK -> dishes (Có thể null nếu khách đánh giá chung nhà hàng)
    private int rating;     // 1 đến 5 sao
    private String comment;
    private LocalDateTime createdAt;

    public Review() {}

    public Review(int id, int customerId, Integer dishId, int rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.dishId = dishId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Integer getDishId() { return dishId; }
    public void setDishId(Integer dishId) { this.dishId = dishId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}