package com.ecomerce.sportscenter.model;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductViewRequest {
    private AppUser user;
    private Product product;
}