package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GoodsHistory extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    User user;

    @ManyToOne
    Goods goods;

    public GoodsHistory(User user, Goods goods) {
        this.user = user;
        this.goods = goods;
    }
}
