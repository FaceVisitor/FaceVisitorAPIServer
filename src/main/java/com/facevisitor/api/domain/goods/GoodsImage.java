package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseImageFileEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = {"goods"})
public class GoodsImage extends BaseImageFileEntity {

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Goods goods;

}
