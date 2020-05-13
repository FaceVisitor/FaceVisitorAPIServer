package com.facevisitor.api.service.order;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.order.OrderLineItem;
import com.facevisitor.api.domain.point.Point;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.OrderLineItemRepository;
import com.facevisitor.api.repository.OrderRepository;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.goods.GoodsUserService;
import com.facevisitor.api.service.personalize.PersonalizeService;
import com.facevisitor.api.service.point.PointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.facevisitor.api.common.string.exception.ExceptionString.BAD_PRICE;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {

    UserRepository userRepository;

    OrderRepository orderRepository;

    OrderLineItemRepository lineItemRepository;

    GoodsUserService goodsUserService;

    GoodsRepository goodsRepository;

    PointService pointService;

    PersonalizeService personalizeService;


    public FVOrder directPay(String userEmail, OrderDTO.OrderDirectPayRequest payRequest) throws JsonProcessingException {
        //set
        FVOrder fvOrder = new FVOrder();
        Goods goods = payRequest.getGoods();
        Goods persistGoods = goodsUserService.get(goods.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setGoods(persistGoods);
        orderLineItem.setGoodsName(persistGoods.getName());
        orderLineItem.setQty(payRequest.getQty());
        orderLineItem.setGoodsPrice(persistGoods.getPrice());
        orderLineItem.setFrontPrice(payRequest.getFrontPrice());

        if (!orderLineItem.validFrontPrice()) {
            throw new BadRequestException(BAD_PRICE);
        }

        fvOrder.addLineItem(orderLineItem);
        fvOrder.setPayPrice(payRequest.getPayPrice());

        User user = userRepository.findByEmail(userEmail).orElseThrow(NotFoundUserException::new);
        fvOrder.setUser(user);

        if (payRequest.getUsePoint() != null && payRequest.getUsePoint().doubleValue() > 0) {
            fvOrder.setPoint(pointService.usePoint(fvOrder, payRequest.getUsePoint()));
        }
        pointService.savePoint(fvOrder);
        if (!fvOrder.validPrice()) {
            throw new BadRequestException(BAD_PRICE);
        }

        return orderRepository.save(fvOrder);
    }

    public FVOrder multiplePay(String userEmail, OrderDTO.OrderMultipleGoodsPayRequest payRequest) {
        //set
        FVOrder fvOrder = new FVOrder();
        User user = userRepository.findByEmail(userEmail).orElseThrow(NotFoundUserException::new);
        fvOrder.setUser(user);

        payRequest.getLineItems().forEach(orderLineItem -> {
            Goods pGoods = goodsRepository.findById(orderLineItem.getGoods().getId()).orElseThrow(NotFoundGoodsException::new);
            orderLineItem.setGoods(pGoods);
            if (!orderLineItem.validFrontPrice()) {
                throw new BadRequestException(BAD_PRICE);
            }
            fvOrder.addLineItem(orderLineItem);
        });

        fvOrder.setPayPrice(payRequest.getPayPrice());


        if (payRequest.getUsePoint() != null && payRequest.getUsePoint().doubleValue() > 0) {
            fvOrder.setPoint(pointService.usePoint(fvOrder, payRequest.getUsePoint()));
        }

        Point point = pointService.savePoint(fvOrder);
        fvOrder.setSavePoint(point);

        if (!fvOrder.validPrice()) {
            throw new BadRequestException(BAD_PRICE);
        }

        return orderRepository.save(fvOrder);
    }

    @Transactional(readOnly = true)
    public FVOrder get(Long order_id) {
        return orderRepository.get(order_id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<FVOrder> page(Pageable pageable, String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return orderRepository.pageable(byEmail.get().getId(), pageable);
        } else {
            throw new NotFoundException();
        }
    }
}
