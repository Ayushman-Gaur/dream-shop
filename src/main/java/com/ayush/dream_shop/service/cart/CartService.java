package com.ayush.dream_shop.service.cart;

import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.Cart;
import com.ayush.dream_shop.model.User;
import com.ayush.dream_shop.repository.CartItemRepository;
import com.ayush.dream_shop.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator= new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(()-> new ResourceNotfoundException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

//    This method is for clearing the cart
//    As this method first going to get the cart by its id
//    than it calls the other method getCart
//    than it ask CartItem repository to delete all the cart item
//    than cart items got cleared
//    then cart is deleted
    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart =getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }


//   In this method there are 2 important things to note
//    As this method first take a variable cart and find that cart using another method getCart
//    than it return total sum amount by first it change all the item into stream(basically a list of item)
//    than it use map for loop purpose as cartItem is getting its totalAmount
//    than at last reduce method is used as it takes 2 parameter one is initial value and other is the method which in going to apply on totalAmount variable
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
//        return cart.getItems()
//                .stream()
//                .map(CartItem ::getTotalPrice)
//                .reduce(BigDecimal.ZERO ,BigDecimal::add);

        return cart.getTotalAmount();
    }



    @Override
    public Cart initializeNewCart(User user){
       return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(()-> {
           Cart cart = new Cart();
           cart.setUser(user);
           return cartRepository.save(cart);
       });
    }
    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
