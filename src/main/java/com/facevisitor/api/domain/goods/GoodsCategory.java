package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"goods","childCategory"})
public class GoodsCategory extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    String name;

    Boolean active = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "GoodsCategory_ChildCategory")
    @JsonIgnore
    Set<GoodsCategory> childCategory;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    Set<Goods> goods = new LinkedHashSet<>();



}
