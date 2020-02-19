package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseImageFileEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class GoodsImage extends BaseImageFileEntity {

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Goods goods;

}
