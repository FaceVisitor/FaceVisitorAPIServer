package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class GoodsCategory extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    Set<Goods> goods = new LinkedHashSet<>();

    Boolean active = true;

}
