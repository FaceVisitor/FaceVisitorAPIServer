package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseImageFileEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GoodsImage extends BaseImageFileEntity {

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne
    Goods goods;

}
