package service;

import dao.ReportDAO;

public class ReportService {
    private ReportDAO reportDAO = new ReportDAO();

    public void showMonthlyRevenue(int month, int year) {
        if (month < 1 || month > 12 || year < 2000) {
            System.err.println("Tháng hoặc năm không hợp lệ!");
            return;
        }
        double revenue = reportDAO.getRevenueByMonthAndYear(month, year);
        System.out.println("💰 Tổng doanh thu Tháng " + month + "/" + year + " là: $" + revenue);
    }

    public void showTopSellingDishes(int top) {
        if (top <= 0) top = 5; // Mặc định lấy Top 5
        reportDAO.printTopSellingDishes(top);
    }
}