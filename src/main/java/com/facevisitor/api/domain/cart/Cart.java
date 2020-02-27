package com.facevisitor.api.domain.cart;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    Goods goods;

    int qty;

    @ManyToOne
    @JsonIgnore
    User user;
}
