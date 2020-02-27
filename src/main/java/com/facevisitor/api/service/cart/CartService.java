package com.facevisitor.api.service.cart;

import com.facevisitor.api.common.exception.NotFoundCartException;
import com.facevisitor.api.domain.cart.Cart;
import com.facevisitor.api.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    final
    CartRepository cartRepository;


    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional(readOnly = true)
    public List<Cart> list(String email){
          return cartRepository.findByUserEmail(email);
    }

    @Transactional(readOnly = true)
    public Cart get(Long id){
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundCartException(id));
    }

    public Cart create(Cart cart){
        Optional<Cart> optionalCart = cartRepository.findByGoods(cart.getGoods());
        if(optionalCart.isPresent()){
            Cart existCart = optionalCart.get();
            existCart.setQty(existCart.getQty() +1);
            return existCart;
        }else{
            return cartRepository.save(cart);
        }
    }

    public Cart updateQty(Long id, int qty){
        Cart target = cartRepository.findById(id).orElseThrow(() -> new NotFoundCartException(id));
        target.setQty(qty);
        return target;
    }


    public void delete(Long id){
        Cart target = cartRepository.findById(id).orElseThrow(() -> new NotFoundCartException(id));
        cartRepository.delete(target);
    }


    public void deleteAll(String email) {
        List<Cart> byUserEmail = cartRepository.findByUserEmail(email);
        cartRepository.deleteAll(byUserEmail);
    }
}
