package com.facevisitor.api.domain.face;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "UserFace")
@Getter
@Setter
@ToString(exclude = {"user","faceId","faceImages"})
public class FaceMeta extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    int lowAge;

    int highAge;

    String gender;

    @OneToMany(mappedBy = "faceMeta",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<FaceId> faceId = new LinkedHashSet<>();

    @OneToMany(mappedBy = "faceMeta", cascade = CascadeType.ALL,orphanRemoval = true)
    List<FaceImage> faceImages = new ArrayList<>();

    public void addFaceImage(FaceImage faceImage){
        faceImage.setFaceMeta(this);
        this.faceImages.add(faceImage);
    }
    public void addFaceId(FaceId faceId){
        faceId.setFaceMeta(this);
        this.faceId.add(faceId);
    }

    @OneToOne(mappedBy = "faceMeta", fetch = FetchType.LAZY)
    @JsonIgnore
    User user;


}
