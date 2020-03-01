package com.facevisitor.api.domain.point;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.facevisitor.api.common.string.exception.ExceptionString.BAD_POINT;


@Entity
@Getter
@Setter
public class Point extends BaseEntity {

    private final static double POINT_PERCENT = 0.07;

    @Id
    @GeneratedValue
    @Column(name = "point_id")
    Long id;

    @Enumerated(value = EnumType.STRING)
    Status status;

    public enum Status {
        PLUS("적립"),
        MINUS("사용");

        private String value;

        Status(String value) {
            this.value = value;
        }
    }

    BigDecimal pointValue;

    @OneToOne(mappedBy = "point")
    @JsonIgnore
    FVOrder order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    User user;


    public static Point savePoint(FVOrder fvOrder) {
        Point point = new Point();
        point.setStatus(Status.PLUS);
        point.setOrder(fvOrder);
        User user = fvOrder.getUser();
        point.setUser(user);
        BigDecimal pointValue = BigDecimal.valueOf(fvOrder.getPayPrice().doubleValue() * POINT_PERCENT)
                .setScale(0,BigDecimal.ROUND_HALF_UP);
        user.savePoint(pointValue);
        point.setPointValue(pointValue);
        fvOrder.setSavePoint(point);
        fvOrder.getUser().getPoints().add(point);
        return point;
    }

    public static Point usePoint(FVOrder fvOrder,BigDecimal usePoint) {
        User user = fvOrder.getUser();
        if(validUsePoint(user,usePoint)){
            Point point = new Point();
            point.setStatus(Status.MINUS);
            point.setOrder(fvOrder);

            point.setUser(user);
            point.setPointValue(usePoint);
            user.usePoint(usePoint);
            fvOrder.setPoint(point);
            fvOrder.getUser().getPoints().add(point);
            return point;
        }else{
            throw new BadRequestException(BAD_POINT);
        }
    }

    public static boolean validUsePoint(User user,BigDecimal usePoint){
        BigDecimal havePoint = user.getPoint();
        return havePoint.doubleValue()>0 && havePoint.doubleValue()>=usePoint.doubleValue();
    }
}
