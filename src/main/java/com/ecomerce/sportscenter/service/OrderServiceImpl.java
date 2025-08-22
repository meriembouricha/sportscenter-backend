package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.DeliveryStatus;
import com.ecomerce.sportscenter.entity.Order;
import com.ecomerce.sportscenter.entity.OrderItem;
import com.ecomerce.sportscenter.model.OrderItemResponse;
import com.ecomerce.sportscenter.model.OrderResponse;
import com.ecomerce.sportscenter.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateDeliveryStatus(Long orderId, DeliveryStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setDeliveryStatus(status);
        orderRepository.save(order);
    }


    private OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .stripeSessionId(order.getStripeSessionId())
                .deliveryStatus(order.getDeliveryStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingPrice(order.getShippingPrice())
                .items(itemResponses)
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .username(order.getUser() != null ? order.getUser().getUsername() : null)
                .build();
    }


    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .productPrice(orderItem.getProduct().getPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
