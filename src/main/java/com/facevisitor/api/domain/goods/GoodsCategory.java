package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GoodsCategory extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    String name;

}
