package com.ecomerce.sportscenter.model;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Integer productId;
    private Integer quantity;
}
