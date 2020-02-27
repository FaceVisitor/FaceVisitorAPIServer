package com.facevisitor.api.domain.order;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.point.Point;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class FVOrder extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    User user;

    @OneToOne(fetch = FetchType.LAZY)
    Point point;

    @OneToOne
    Point savePoint;

    @OrderBy("id asc")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "order")
    Set<OrderLineItem> lineItems = new LinkedHashSet<>();

    public void addLineItem(OrderLineItem orderLineItem){
        orderLineItem.setOrder(this);
        lineItems.add(orderLineItem);
    }

    BigDecimal payPrice;



    public boolean validPrice(){
        BigDecimal frontSum = BigDecimal.valueOf(lineItems.stream().mapToDouble(item -> item.getFrontPrice().doubleValue()).sum());
        if(point != null){
            return payPrice.doubleValue() == frontSum.subtract(this.point.getPointValue()).doubleValue();
        }else{
            return payPrice.doubleValue() == frontSum.doubleValue();
        }
    }

}
