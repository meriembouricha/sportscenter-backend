package com.ecomerce.sportscenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopBrandDTO {
    private String brandName;
    private Long quantitySold;
}
