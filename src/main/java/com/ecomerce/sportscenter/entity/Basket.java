package com.ecomerce.sportscenter.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@RedisHash("Basket")
public class Basket {
    @Id
    private String id;
    @Indexed
    private Long userId;
    private List<BasketItem> items = new ArrayList<>();
    private Long shippingPrice = 0L;
    public Basket(String id){
        this.id = id;
    }
}
