package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseImageEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GoodsImage extends BaseImageEntity {

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne
    Goods goods;

}
