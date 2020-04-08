package com.facevisitor.api.service.order;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.order.OrderLineItem;
import com.facevisitor.api.domain.point.Point;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.OrderLineItemRepository;
import com.facevisitor.api.repository.OrderRepository;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.goods.GoodsUserService;
import com.facevisitor.api.service.point.PointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.facevisitor.api.common.string.exception.ExceptionString.BAD_PRICE;

@Service
@Transactional
public class OrderService {
    final
    UserRepository userRepository;

    final
    OrderRepository orderRepository;

    final
    OrderLineItemRepository lineItemRepository;

    final
    GoodsUserService goodsUserService;

    final
    GoodsRepository goodsRepository;

    final
    PointService pointService;


    public OrderService(OrderRepository orderRepository, OrderLineItemRepository lineItemRepository, GoodsUserService goodsUserService, UserRepository userRepository, GoodsRepository goodsRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.lineItemRepository = lineItemRepository;
        this.goodsUserService = goodsUserService;
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
        this.pointService = pointService;
    }

    public FVOrder directPay(String userEmail, OrderDTO.OrderDirectPayRequest payRequest) {
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


        fvOrder.setUser(userRepository.findByEmail(userEmail).orElseThrow(NotFoundUserException::new));

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
        payRequest.getLineItems().forEach(orderLineItem -> {
            Goods pGoods = goodsRepository.findById(orderLineItem.getGoods().getId()).orElseThrow(NotFoundGoodsException::new);
            orderLineItem.setGoods(pGoods);
            if (!orderLineItem.validFrontPrice()) {
                throw new BadRequestException(BAD_PRICE);
            }
            fvOrder.addLineItem(orderLineItem);
        });

        fvOrder.setPayPrice(payRequest.getPayPrice());

        fvOrder.setUser(userRepository.findByEmail(userEmail).orElseThrow(NotFoundUserException::new));

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
    public FVOrder get(Long order_id){
        return  orderRepository.get(order_id).orElseThrow(NotFoundException::new);
    }
}
