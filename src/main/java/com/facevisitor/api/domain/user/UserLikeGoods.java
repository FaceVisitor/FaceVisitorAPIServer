package com.facevisitor.api.domain.user;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "UserLikeGoods")
@Getter
@Setter
public class UserLikeGoods extends BaseEntity {

    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(nullable = false,name = "user_id")
    @ManyToOne
    User user;

    @JoinColumn(nullable = false,name = "goods_id")
    @ManyToOne
    Goods goods;

    //선호도
    Long prefer;
}
