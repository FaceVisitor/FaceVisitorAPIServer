package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Goods extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String vendor;

    String price;


    @OneToMany(mappedBy = "goods" , cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<GoodsImage> images;

    @JoinTable(name = "GoodsToCategory", joinColumns = {@JoinColumn(name = "goods_id",nullable = false)},inverseJoinColumns = {@JoinColumn(name = "category_id",nullable = false)})
    @ManyToMany
    List<GoodsCategory> categories = new ArrayList<>();


}
