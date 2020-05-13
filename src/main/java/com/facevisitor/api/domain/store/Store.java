package com.facevisitor.api.domain.store;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.UserToStore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@ToString(exclude = {"images", "user", "orders"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    String name;

    String address;

    @Lob
    String description;

    String openTime;

    String phone;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Goods> goods = new LinkedHashSet<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<StoreImage> images = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    User user;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    Set<UserToStore> userToStores = new LinkedHashSet<>();

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    List<FVOrder> orders = new ArrayList<>();

    public void addGood(Goods goods) {
        goods.setStore(this);
        this.goods.add(goods);
    }

    public void addGoods(List<Goods> goods) {
        goods.forEach(good -> {
            good.setStore(this);
            this.goods.add(good);
        });
    }

    public void addImages(List<StoreImage> storeImage) {
        storeImage.forEach(image -> {
            this.images.add(image);
            image.setStore(this);
        });
    }

    public void addImage(StoreImage storeImage) {
        storeImage.setStore(this);
        this.images.add(storeImage);
    }

    public void deleteImage(String url) {
        this.getImages().remove(this.getImages().stream().filter(storeImage -> storeImage.getUrl().equals(url)).findFirst().orElseThrow(NotFoundException::new));
    }


}
