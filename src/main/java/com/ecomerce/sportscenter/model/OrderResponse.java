package com.ecomerce.sportscenter.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String stripeSessionId;
    private String deliveryStatus;
    private Long totalAmount;
    private Long shippingPrice;
    private List<OrderItemResponse> items;
    private Long userId;
    private String userEmail;
    private String username;
}
