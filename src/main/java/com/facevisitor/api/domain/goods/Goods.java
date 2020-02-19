package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Goods extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String vendor;

    BigDecimal price;

    BigDecimal salePrice;

    Boolean active = true;

    @OneToMany(mappedBy = "goods" , cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<GoodsImage> images = new LinkedHashSet<>();

    public void addImage(GoodsImage goodsImage){
        this.images.add(goodsImage);
        goodsImage.setGoods(this);
    }

    public void addImages(Collection<GoodsImage> goodsImage){
        this.images.addAll(goodsImage);
        goodsImage.forEach(image -> {
            image.setGoods(this);
        });
    }


    @JoinTable(name = "GoodsToCategory", joinColumns = {@JoinColumn(name = "goods_id",nullable = false)},inverseJoinColumns = {@JoinColumn(name = "category_id",nullable = false)})
    @ManyToMany
    Set<GoodsCategory> categories = new LinkedHashSet<>();

    public void addCategory(GoodsCategory goodsCategory){
        this.categories.add(goodsCategory);
        goodsCategory.getGoods().add(this);
    }


    public void deleteImage(String url) {
        this.images.removeIf(goodsImage -> goodsImage.getUrl().equals(url));
    }
}
