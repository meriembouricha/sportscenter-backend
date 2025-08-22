package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.model.dto.DailySalesDTO;
import com.ecomerce.sportscenter.model.dto.MonthlySalesDTO;
import com.ecomerce.sportscenter.repository.IAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final IAnalyticsRepository analyticsRepository;

    public List<DailySalesDTO> getDailySalesForMonthComplete(int year, int month) {
        List<Object[]> rawData = analyticsRepository.getDailySalesForMonth(year, month);
        Map<Integer, Long> salesMap = new HashMap<>();
        for (Object[] row : rawData) {
            Integer day = ((Number) row[0]).intValue();
            Long total = ((Number) row[1]).longValue();
            salesMap.put(day, total);
        }

        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        List<DailySalesDTO> result = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            result.add(new DailySalesDTO(day, salesMap.getOrDefault(day, 0L)));
        }

        return result;
    }

    public List<MonthlySalesDTO> getMonthlySalesForYearComplete(int year) {
        List<Object[]> rawData = analyticsRepository.getMonthlySalesForYear(year);
        Map<Integer, Long> salesMap = new HashMap<>();
        for (Object[] row : rawData) {
            Integer month = ((Number) row[0]).intValue();
            Long total = ((Number) row[1]).longValue();
            salesMap.put(month, total);
        }

        List<MonthlySalesDTO> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new MonthlySalesDTO(month, salesMap.getOrDefault(month, 0L)));
        }

        return result;
    }


    public List<Object[]> getBestSellingProducts() {
        return analyticsRepository.getBestSellingProducts();
    }

    public List<Object[]> getTopBrandsBySales() {
        return analyticsRepository.getTopBrandsBySales();
    }

    public List<Object[]> getPopularCategories() {
        return analyticsRepository.getPopularCategories();
    }
}
