package com.ayush.dream_shop.request;

import com.ayush.dream_shop.model.Category;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductUpdateRequest {
    private Long it;
    private String name;
    private String brand;
    private BigDecimal price;
    private  int inventory;
    private String description;

    private Category category;
}
