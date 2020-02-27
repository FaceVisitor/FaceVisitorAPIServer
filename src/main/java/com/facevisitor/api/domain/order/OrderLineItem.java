package com.facevisitor.api.domain.order;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString(exclude = {"order","goods"})
public class OrderLineItem extends BaseEntity {

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    FVOrder order;

    String goodsName;

    BigDecimal goodsPrice;

    int qty;

    BigDecimal frontPrice;

    public boolean validFrontPrice(){
        return this.frontPrice.doubleValue() == goodsPrice.doubleValue() * qty;
    }


}
