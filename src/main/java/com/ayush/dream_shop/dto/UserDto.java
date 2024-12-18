package com.ayush.dream_shop.dto;

import com.ayush.dream_shop.model.Cart;
import com.ayush.dream_shop.model.Order;
import lombok.Data;

import java.util.List;
@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private List<OrderDto> orders;
    private CartDto cart;

}
