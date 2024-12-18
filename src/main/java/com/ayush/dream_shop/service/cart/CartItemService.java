package com.ayush.dream_shop.service.cart;

import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.Cart;
import com.ayush.dream_shop.model.CartItem;
import com.ayush.dream_shop.model.Product;
import com.ayush.dream_shop.repository.CartItemRepository;
import com.ayush.dream_shop.repository.CartRepository;
import com.ayush.dream_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;



//    Get the Cart
//    Get the product
//    check if the item is already there or not
//    if yes than increase the quantity with the requested quantity
//    if no the initiate the new cart item entery for
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart= cartService.getCart(cartId);
        Product product= productService.getProductById(productId);
        CartItem cartItem= cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());

        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() +quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart= cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId ,productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart= cartService.getCart(cartId);
        cart.getItems()
                .stream().
                filter(item->item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }


    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return  cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()-> new ResourceNotfoundException("Product not found"));
    }
}
