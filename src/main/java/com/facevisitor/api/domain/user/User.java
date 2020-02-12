package com.facevisitor.api.domain.user;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.security.Authority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"authorities", "faceMeta"})
public class User extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "face_meta_id")
    @JsonIgnore
    FaceMeta faceMeta;

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String name;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserToAuthority", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "auth_id")})
    private Set<Authority> authorities = new LinkedHashSet<>();

    private Boolean enable = true;


    public void addFaceMeta(FaceMeta meta) {
        meta.setUser(this);
        this.setFaceMeta(meta);
    }




}
