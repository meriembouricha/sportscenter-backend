package com.ecomerce.sportscenter.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalyticsRepository implements IAnalyticsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> getDailySalesForMonth(int year, int month) {
        return entityManager.createQuery("""
            SELECT FUNCTION('DAY', o.createdAt), SUM(o.totalAmount)
            FROM Order o
            WHERE FUNCTION('YEAR', o.createdAt) = :year AND FUNCTION('MONTH', o.createdAt) = :month
            GROUP BY FUNCTION('DAY', o.createdAt)
            ORDER BY FUNCTION('DAY', o.createdAt)
        """, Object[].class)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public List<Object[]> getMonthlySalesForYear(int year) {
        return entityManager.createQuery("""
            SELECT FUNCTION('MONTH', o.createdAt), SUM(o.totalAmount)
            FROM Order o
            WHERE FUNCTION('YEAR', o.createdAt) = :year
            GROUP BY FUNCTION('MONTH', o.createdAt)
            ORDER BY FUNCTION('MONTH', o.createdAt)
        """, Object[].class)
                .setParameter("year", year)
                .getResultList();
    }


    @Override
    public List<Object[]> getBestSellingProducts() {
        return entityManager.createQuery(
                "SELECT oi.product.name, SUM(oi.quantity), SUM(oi.quantity * oi.product.price) " +
                        "FROM OrderItem oi GROUP BY oi.product.name ORDER BY SUM(oi.quantity) DESC",
                Object[].class
        ).getResultList();
    }

    @Override
    public List<Object[]> getTopBrandsBySales() {
        return entityManager.createQuery(
                "SELECT p.brand.name, SUM(oi.quantity) " +
                        "FROM OrderItem oi JOIN oi.product p " +
                        "GROUP BY p.brand.name ORDER BY SUM(oi.quantity) DESC",
                Object[].class
        ).getResultList();
    }

    @Override
    public List<Object[]> getPopularCategories() {
        return entityManager.createQuery(
                "SELECT p.type.name, SUM(oi.quantity) " +
                        "FROM OrderItem oi JOIN oi.product p " +
                        "GROUP BY p.type.name ORDER BY SUM(oi.quantity) DESC",
                Object[].class
        ).getResultList();
    }
}
