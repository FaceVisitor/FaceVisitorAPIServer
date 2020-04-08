package com.facevisitor.api.controller;

import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.cart.Cart;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.cart.CartService;
import com.facevisitor.api.service.goods.GoodsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {

    CartService cartService;

    UserRepository userRepository;

    GoodsUserService goodsUserService;

    @GetMapping
    public ResponseEntity list(Principal principal){
        List<Cart> list = cartService.list(principal.getName());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity create(Principal principal, @RequestBody Cart cart){
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(NotFoundUserException::new);
        Goods goods = goodsUserService.get(cart.getGoods().getId());
        cart.setUser(user);
        cart.setGoods(goods);
        Cart saved = cartService.create(cart);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{cart_id}")
    public ResponseEntity delete(@PathVariable Long cart_id){
        cartService.delete(cart_id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity deleteAll(Principal principal){
        cartService.deleteAll(principal.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{cart_id}/{qty}")
    public ResponseEntity updateQty(@PathVariable Long cart_id, @PathVariable int qty){
        return ResponseEntity.ok( cartService.updateQty(cart_id,qty));
    }
}
