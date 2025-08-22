package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.model.dto.DailySalesDTO;
import com.ecomerce.sportscenter.model.dto.MonthlySalesDTO;
import com.ecomerce.sportscenter.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales/daily-complete")
    public ResponseEntity<List<DailySalesDTO>> getDailySalesForMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(analyticsService.getDailySalesForMonthComplete(year, month));
    }

    @GetMapping("/sales/monthly")
    public ResponseEntity<List<MonthlySalesDTO>> getMonthlySalesForYear(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(analyticsService.getMonthlySalesForYearComplete(year));
    }


    @GetMapping("/best-products")
    public ResponseEntity<List<Object[]>> getBestSellingProducts() {
        return ResponseEntity.ok(analyticsService.getBestSellingProducts());
    }

    @GetMapping("/top-brands")
    public ResponseEntity<List<Object[]>> getTopBrandsBySales() {
        return ResponseEntity.ok(analyticsService.getTopBrandsBySales());
    }

    @GetMapping("/popular-categories")
    public ResponseEntity<List<Object[]>> getPopularCategories() {
        return ResponseEntity.ok(analyticsService.getPopularCategories());
    }
}
