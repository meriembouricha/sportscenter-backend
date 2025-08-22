package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.DeliveryStatus;
import com.ecomerce.sportscenter.model.OrderResponse;
import com.ecomerce.sportscenter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/status")
//    @PreAuthorize("hasRole('LIVREUR')")
    public void updateDeliveryStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus status) {
        orderService.updateDeliveryStatus(orderId, status);
    }
}
