package com.ecomerce.sportscenter.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private Integer productId;
    private String productName;
    private Long productPrice;
    private Integer quantity;
}
