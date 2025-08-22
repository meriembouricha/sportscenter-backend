package com.ecomerce.sportscenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestProductDTO {
    private String productName;
    private Long quantitySold;
    private Long totalRevenue;
}