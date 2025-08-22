package com.ecomerce.sportscenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularCategoryDTO {
    private String categoryName;
    private Long quantitySold;
}