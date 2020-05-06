package com.facevisitor.api.controller;


import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.service.order.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class FvOrderController {

    OrderService orderService;

    ModelMapper modelMapper;

    @GetMapping("/{order_id}")
    ResponseEntity get(@PathVariable Long order_id) {
        FVOrder fvOrder = orderService.get(order_id);
        OrderDTO.OrderDetailResponse detailResponse = modelMapper.map(fvOrder, OrderDTO.OrderDetailResponse.class);
        double sum = detailResponse.getLineItems().stream().mapToDouble(orderLineItem -> orderLineItem.getFrontPrice().doubleValue()).sum();
        detailResponse.setOriginalPrice(BigDecimal.valueOf(sum));
        return ResponseEntity.ok(detailResponse);
    }

    @GetMapping
    ResponseEntity page(@PageableDefault Pageable pageable, Principal principal) {
        String email = principal.getName();
        System.out.println(email);
        Page<FVOrder> page = orderService.page(pageable, email);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/pay")
    ResponseEntity pay(Principal principal, @RequestBody OrderDTO.OrderDirectPayRequest payRequest) throws JsonProcessingException {
        if (principal == null) {
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(orderService.directPay(principal.getName(), payRequest));
    }

    @PostMapping("/pay/multiple")
    ResponseEntity multiplePay(Principal principal , @RequestBody OrderDTO.OrderMultipleGoodsPayRequest payRequest){
        if(principal == null){
            throw new UnAuthorizedException();
        }
        return ResponseEntity.ok(orderService.multiplePay(principal.getName(),payRequest));
    }

}
