package com.ecomerce.sportscenter.repository;

import com.ecomerce.sportscenter.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
