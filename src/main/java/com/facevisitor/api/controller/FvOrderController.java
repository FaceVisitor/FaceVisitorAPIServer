package com.facevisitor.api.controller;


import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/order")
public class FvOrderController {

    final
    OrderService orderService;

    final
    ModelMapper modelMapper;

    public FvOrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{order_id}")
    ResponseEntity get(@PathVariable  Long order_id){
        FVOrder fvOrder = orderService.get(order_id);
        OrderDTO.OrderDetailResponse detailResponse = modelMapper.map(fvOrder, OrderDTO.OrderDetailResponse.class);
        double sum = detailResponse.getLineItems().stream().mapToDouble(orderLineItem -> orderLineItem.getFrontPrice().doubleValue()).sum();
        detailResponse.setOriginalPrice(BigDecimal.valueOf(sum));
        return ResponseEntity.ok(detailResponse);
    }

    @GetMapping()
    ResponseEntity list(){
        return null;
    }

    @PostMapping("/pay")
    ResponseEntity pay(Principal principal , @RequestBody OrderDTO.OrderDirectPayRequest payRequest){
        if(principal == null){
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(orderService.directPay(principal.getName(),payRequest));
    }

    @PostMapping("/pay/multiple")
    ResponseEntity multiplePay(Principal principal , @RequestBody OrderDTO.OrderMultipleGoodsPayRequest payRequest){
        if(principal == null){
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(orderService.payMultipleGoods(principal.getName(),payRequest));
    }

}
