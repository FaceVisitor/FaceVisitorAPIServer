package com.facevisitor.api.dto.order;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.OrderLineItem;
import com.facevisitor.api.domain.point.Point;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderDTO {

    @Data
    public static class OrderDirectPayRequest {
        Goods goods;
        int qty;
        BigDecimal usePoint;
        BigDecimal frontPrice;
        BigDecimal payPrice;
        String stripeToken;
    }

    @Data
    public static class OrderMultipleGoodsPayRequest{
        @NotNull
        Set<OrderLineItem> lineItems;
        BigDecimal usePoint;
        @NotNull
        BigDecimal payPrice;
        String stripeToken;
    }

    @Data
    public static class OrderCartPayRequest{
        List<Goods> goods;
        BigDecimal usePoint;
        BigDecimal payPrice;
        String stripeToken;
    }

    @Data
    public static class OrderListResponse{

    }

    @Data
    public static class OrderDetailResponse{
        Long id;
        Point point;
        Point savePoint;
        Set<OrderLineItem> lineItems = new LinkedHashSet<>();
        BigDecimal payPrice;
        BigDecimal originalPrice;
    }
}
