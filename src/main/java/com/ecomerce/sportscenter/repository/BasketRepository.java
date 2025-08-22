package com.ecomerce.sportscenter.repository;

import com.ecomerce.sportscenter.entity.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends CrudRepository<Basket, String> {
    Basket findByUserId(Long userId);
}