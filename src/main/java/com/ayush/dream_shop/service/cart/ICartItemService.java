package com.ayush.dream_shop.service.cart;

import com.ayush.dream_shop.model.Cart;
import com.ayush.dream_shop.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId, int quantity);
    void removeItemFromCart (Long cartId , Long productId);
    void updateItemQuantity(Long cartId ,Long productId ,int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
