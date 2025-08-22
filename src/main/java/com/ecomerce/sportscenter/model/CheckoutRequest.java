package com.ecomerce.sportscenter.model;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private Long userId;
    private List<OrderItemRequest> items;
}
