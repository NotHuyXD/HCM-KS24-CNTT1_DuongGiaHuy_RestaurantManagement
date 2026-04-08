package service;

import dao.ReviewDAO;
import model.feedback.Review;

import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }

    public boolean addReview(int customerId, Integer dishId, int rating, String comment) {
        // Validation số sao đánh giá
        if (rating < 1 || rating > 5) {
            System.err.println("Điểm đánh giá phải từ 1 đến 5 sao!");
            return false;
        }
        if (comment == null || comment.trim().isEmpty()) {
            System.err.println("Vui lòng nhập nội dung đánh giá!");
            return false;
        }

        Review review = new Review();
        review.setCustomerId(customerId);
        review.setDishId(dishId);
        review.setRating(rating);
        review.setComment(comment);

        return reviewDAO.insert(review);
    }

    public List<Review> getAllReviews() {
        return reviewDAO.findAll();
    }

    public List<Review> getReviewsByDish(int dishId) {
        return reviewDAO.findByDishId(dishId);
    }
}