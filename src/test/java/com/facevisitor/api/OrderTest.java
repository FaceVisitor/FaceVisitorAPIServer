package com.facevisitor.api;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.order.OrderLineItem;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.repository.GoodsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTest extends BaseTest {

    String userBaseUrl = "/api/v1/order";


    @Autowired
    GoodsRepository goodsRepository;

    double point_pecent = 0.07;

    @Test
    public void 주문_결제_포인트사용X() throws Exception {
        User owner = createOwner();
        owner.setPoint(BigDecimal.ZERO);
        OrderDTO.OrderDirectPayRequest payRequest = new OrderDTO.OrderDirectPayRequest();
        payRequest.setFrontPrice(BigDecimal.valueOf(100L));
        Goods goods = new Goods();
        goods.setPrice(BigDecimal.valueOf(100L));
        goods.setName("test goods");
        Goods savedGoods = goodsRepository.save(goods);

        payRequest.setGoods(savedGoods);
        payRequest.setQty(1);
        payRequest.setStripeToken("Test");
        payRequest.setUsePoint(null);
        payRequest.setPayPrice(BigDecimal.valueOf(100L));

        mockMvc.perform(postWithUser(userBaseUrl + "/pay")
                .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        BigDecimal point = owner.getPoint();
        assertThat(point).isEqualTo(calSavePoint(payRequest.getPayPrice()));
    }

    @Test
    public void 주문_결제_포인트사용O() throws Exception {
        BigDecimal productPrice = BigDecimal.valueOf(100);
        BigDecimal originalPoint = BigDecimal.valueOf(100);
        BigDecimal usePoint = BigDecimal.valueOf(10);
        BigDecimal finalPoint;
        User owner = createOwner();
        owner.setPoint(originalPoint);
        OrderDTO.OrderDirectPayRequest payRequest = new OrderDTO.OrderDirectPayRequest();

        Goods goods = new Goods();
        goods.setPrice(productPrice);
        goods.setName("test goods");
        Goods savedGoods = goodsRepository.save(goods);

        payRequest.setFrontPrice(productPrice);
        payRequest.setGoods(savedGoods);
        payRequest.setQty(1);
        payRequest.setStripeToken("Test");
        payRequest.setUsePoint(BigDecimal.valueOf(10));
        payRequest.setPayPrice(productPrice.subtract(usePoint));
        finalPoint = calSavePoint(payRequest.getPayPrice());
        mockMvc.perform(postWithUser(userBaseUrl + "/pay")
                .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        assertThat(owner.getPoint()).isEqualTo(originalPoint.subtract(usePoint).add(finalPoint));
    }

    @Test
    public void 주문_여러상품_결제_포인트사용X() throws Exception {
        User owner = createOwner();
        OrderDTO.OrderMultipleGoodsPayRequest payRequest = new OrderDTO.OrderMultipleGoodsPayRequest();
        Set<OrderLineItem> orderLineItemList = new LinkedHashSet<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        Goods goods = new Goods();
        goods.setPrice(BigDecimal.valueOf(100L));
        goods.setName("test goods");
        Goods savedGoods = goodsRepository.save(goods);

        Goods goods2 = new Goods();
        goods2.setPrice(BigDecimal.valueOf(200L));
        goods2.setName("test goods2");
        Goods savedGoods2 = goodsRepository.save(goods);

        orderLineItem.setGoods(savedGoods);
        orderLineItem.setQty(1);
        orderLineItem.setFrontPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsName(goods.getName());
        orderLineItemList.add(orderLineItem);

        orderLineItem2.setGoods(savedGoods2);
        orderLineItem2.setQty(2);
        orderLineItem2.setFrontPrice(BigDecimal.valueOf(400));
        orderLineItem2.setGoodsPrice(BigDecimal.valueOf(200));
        orderLineItem2.setGoodsName(goods2.getName());
        orderLineItemList.add(orderLineItem2);

        payRequest.setStripeToken("Test");
        payRequest.setUsePoint(null);
        payRequest.setPayPrice(BigDecimal.valueOf(500L));
        payRequest.setLineItems(orderLineItemList);

        mockMvc.perform(postWithUser(userBaseUrl + "/pay/multiple")
                .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.lineItems").isArray())
                .andExpect(jsonPath("$.lineItems[0].qty").exists())
                .andExpect(jsonPath("$.lineItems[1].qty").exists())
                .andExpect(jsonPath("$.payPrice").value(BigDecimal.valueOf(500)))
                .andDo(print());

        assertThat(owner.getPoint()).isEqualTo(calSavePoint(payRequest.getPayPrice()));

    }

    @Test
    public void 주문_여러상품_결제_포인트사용O() throws Exception {
        BigDecimal havePoint = BigDecimal.valueOf(100);
        BigDecimal usePoint = BigDecimal.valueOf(50);
        BigDecimal nowPoint = havePoint.subtract(usePoint);
        BigDecimal payPrice = BigDecimal.ZERO;
        User owner = createOwner();
        owner.setPoint(havePoint);

        OrderDTO.OrderMultipleGoodsPayRequest payRequest = new OrderDTO.OrderMultipleGoodsPayRequest();
        Set<OrderLineItem> orderLineItemList = new LinkedHashSet<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        Goods goods = new Goods();
        goods.setPrice(BigDecimal.valueOf(100L));
        goods.setName("test goods");
        Goods savedGoods = goodsRepository.save(goods);

        Goods goods2 = new Goods();
        goods2.setPrice(BigDecimal.valueOf(200L));
        goods2.setName("test goods2");
        Goods savedGoods2 = goodsRepository.save(goods);

        orderLineItem.setGoods(savedGoods);
        orderLineItem.setQty(1);
        orderLineItem.setFrontPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsName(goods.getName());
        orderLineItemList.add(orderLineItem);

        orderLineItem2.setGoods(savedGoods2);
        orderLineItem2.setQty(2);
        orderLineItem2.setFrontPrice(BigDecimal.valueOf(400));
        orderLineItem2.setGoodsPrice(BigDecimal.valueOf(200));
        orderLineItem2.setGoodsName(goods2.getName());
        orderLineItemList.add(orderLineItem2);

        payPrice = orderLineItem.getFrontPrice().add(orderLineItem2.getFrontPrice()).subtract(usePoint);

        payRequest.setStripeToken("Test");
        //use point
        payRequest.setUsePoint(usePoint);
        payRequest.setPayPrice(payPrice);
        payRequest.setLineItems(orderLineItemList);

        mockMvc.perform(postWithUser(userBaseUrl + "/pay/multiple")
                .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.lineItems").isArray())
                .andExpect(jsonPath("$.lineItems[0].qty").exists())
                .andExpect(jsonPath("$.lineItems[1].qty").exists())
                .andExpect(jsonPath("$.payPrice").value(payPrice))
                .andDo(print());

        assertThat(owner.getPoint()).isEqualTo(nowPoint);
    }

    @Test
    public void 주문조회_디테일() throws Exception {
        BigDecimal havePoint = BigDecimal.valueOf(100);
        BigDecimal usePoint = BigDecimal.valueOf(50);
        BigDecimal nowPoint = havePoint.subtract(usePoint);
        BigDecimal payPrice = BigDecimal.ZERO;
        User owner = createOwner();
        owner.setPoint(havePoint);

        OrderDTO.OrderMultipleGoodsPayRequest payRequest = new OrderDTO.OrderMultipleGoodsPayRequest();
        Set<OrderLineItem> orderLineItemList = new LinkedHashSet<>();
        OrderLineItem orderLineItem = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        Goods goods = new Goods();
        goods.setPrice(BigDecimal.valueOf(100L));
        goods.setName("test goods");
        Goods savedGoods = goodsRepository.save(goods);

        Goods goods2 = new Goods();
        goods2.setPrice(BigDecimal.valueOf(200L));
        goods2.setName("test goods2");
        Goods savedGoods2 = goodsRepository.save(goods);

        orderLineItem.setGoods(savedGoods);
        orderLineItem.setQty(1);
        orderLineItem.setFrontPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsPrice(BigDecimal.valueOf(100));
        orderLineItem.setGoodsName(goods.getName());
        orderLineItemList.add(orderLineItem);

        orderLineItem2.setGoods(savedGoods2);
        orderLineItem2.setQty(2);
        orderLineItem2.setFrontPrice(BigDecimal.valueOf(400));
        orderLineItem2.setGoodsPrice(BigDecimal.valueOf(200));
        orderLineItem2.setGoodsName(goods2.getName());
        orderLineItemList.add(orderLineItem2);

        payPrice = orderLineItem.getFrontPrice().add(orderLineItem2.getFrontPrice()).subtract(usePoint);

        payRequest.setStripeToken("Test");
        //use point
        payRequest.setUsePoint(usePoint);
        payRequest.setPayPrice(payPrice);
        payRequest.setLineItems(orderLineItemList);

        String contentAsString = mockMvc.perform(postWithUser(userBaseUrl + "/pay/multiple")
                .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.lineItems").isArray())
                .andExpect(jsonPath("$.lineItems[0].qty").exists())
                .andExpect(jsonPath("$.lineItems[1].qty").exists())
                .andExpect(jsonPath("$.payPrice").value(payPrice))
                .andDo(print()).andReturn().getResponse().getContentAsString();

        FVOrder fvOrder = gson.fromJson(contentAsString, FVOrder.class);

        mockMvc.perform(getWithUser(userBaseUrl + "/" + fvOrder.getId()))
                .andExpect(jsonPath("$.id").value(fvOrder.getId()))
                .andDo(print());
    }

    public BigDecimal calSavePoint(BigDecimal payPrice){
        return BigDecimal.valueOf(payPrice.doubleValue() * point_pecent).setScale(0,BigDecimal.ROUND_HALF_UP);
    }


}
