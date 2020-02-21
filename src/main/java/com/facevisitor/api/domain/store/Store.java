package com.facevisitor.api.domain.store;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@ToString(exclude = {"images","user"})
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

    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Goods> goods = new LinkedHashSet<>();

    public void addGood(Goods goods){
        goods.setStore(this);
        this.goods.add(goods);
    }

    public void addGoods(List<Goods> goods){
        goods.forEach(good -> {
            good.setStore(this);
            this.goods.add(good);
        });
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    List<StoreImage> images = new ArrayList<>();

    public void addImages(List<StoreImage> storeImage){
        storeImage.forEach(image -> {
            this.images.add(image);
            image.setStore(this);
        });
    }

    public void addImage(StoreImage storeImage){
        this.images.add(storeImage);
    }

    public void deleteImage(String url){
        this.getImages().remove(this.getImages().stream().filter(storeImage -> storeImage.getUrl().equals(url)).findFirst().orElseThrow(NotFoundException::new));
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    User user;


}
