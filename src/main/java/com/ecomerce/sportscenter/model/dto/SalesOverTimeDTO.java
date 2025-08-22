package com.ecomerce.sportscenter.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesOverTimeDTO {
    private String date;
    private Long totalAmount;
}
