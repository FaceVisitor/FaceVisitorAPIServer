package com.facevisitor.api.domain.user;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.cart.Cart;
import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.point.Point;
import com.facevisitor.api.domain.security.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Getter
@Setter
@ToString(exclude = {"authorities", "faceMeta", "goodsLike", "points", "carts", "orders"})
public class User extends BaseEntity {

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "face_meta_id")
    @JsonIgnore
    FaceMeta faceMeta;

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;

    private String name;
    private String phone;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserToAuthority", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "auth_id")})
    private Set<Authority> authorities = new LinkedHashSet<>();

    public void addAuth(Authority authority) {
        this.authorities.add(authority);
    }

    private Boolean enable = true;

    @ManyToMany(cascade = REMOVE)
    @JoinTable(name = "GoodsLike", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "goods_id", nullable = false))
    @JsonIgnore
    Set<Goods> goodsLike = new LinkedHashSet<>();

    public void addGoodsLike(Goods goods) {
        this.goodsLike.add(goods);
    }

    public void removeGoodsLike(Long id) {
        goodsLike.removeIf(goods -> goods.getId().equals(id));
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = ALL)
    @JsonIgnore
    Set<Cart> carts = new LinkedHashSet<>();

    //보유 포인트
    BigDecimal point = BigDecimal.ZERO;

    @OneToMany(fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true, mappedBy = "user")
    @JsonIgnore
    List<Point> points = new ArrayList<>();

    public void addPoint(Point point) {
        point.setUser(this);
        this.points.add(point);
    }

    public void addFaceMeta(FaceMeta meta) {
        meta.setUser(this);
        this.setFaceMeta(meta);
    }

    public void savePoint(BigDecimal point) {
        this.point = this.point.add(point);
    }

    public void usePoint(BigDecimal point) {
        if (this.point.doubleValue() < point.doubleValue()) {
            throw new BadRequestException("포인트가 부족합니다");
        }
        this.point = this.point.subtract(point);
    }

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<FVOrder> orders = new ArrayList<>();


}
