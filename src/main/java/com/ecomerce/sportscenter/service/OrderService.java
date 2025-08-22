package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.DeliveryStatus;
import com.ecomerce.sportscenter.model.OrderResponse;
import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders();
    void updateDeliveryStatus(Long orderId, DeliveryStatus status);
}
