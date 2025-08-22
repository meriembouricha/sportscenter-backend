package com.ecomerce.sportscenter.repository;

import java.util.List;

public interface IAnalyticsRepository {
    List<Object[]> getDailySalesForMonth(int year, int month);
    List<Object[]> getMonthlySalesForYear(int year);
    List<Object[]> getBestSellingProducts();
    List<Object[]> getTopBrandsBySales();
    List<Object[]> getPopularCategories();
}
