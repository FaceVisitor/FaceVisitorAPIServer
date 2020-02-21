package com.facevisitor.api.domain.goods;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"categories", "images"})
public class Goods extends BaseEntity {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String vendor;

    BigDecimal price;

    BigDecimal salePrice;

    @Lob
    String description;

    Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Store store;

    @OneToMany(mappedBy = "goods", cascade = {CascadeType.ALL}, orphanRemoval = true,fetch = FetchType.EAGER)
    Set<GoodsImage> images = new LinkedHashSet<>();

    public void addImage(GoodsImage goodsImage) {
        goodsImage.setGoods(this);
        this.images.add(goodsImage);
    }

    public void addImages(Collection<GoodsImage> goodsImage) {
        goodsImage.forEach(image -> {
            image.setGoods(this);
        });
        this.images.addAll(goodsImage);
    }


    @JoinTable(name = "GoodsToCategory", joinColumns = {@JoinColumn(name = "goods_id", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "category_id", nullable = false)})
    @ManyToMany(fetch = FetchType.EAGER)
    Set<GoodsCategory> categories = new LinkedHashSet<>();

    public void addCategory(GoodsCategory goodsCategory) {
        this.categories.add(goodsCategory);
        goodsCategory.getGoods().add(this);
    }


    public void deleteImage(String url) {
        this.images.removeIf(goodsImage -> goodsImage.getUrl().equals(url));
    }

}
