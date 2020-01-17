package com.facevisitor.api.domain.face;

import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "UserFace")
@Getter
@Setter
@ToString(exclude = {"user"})
public class FaceMeta extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    int lowAge;

    int highAge;

    String gender;

    @ElementCollection
    @CollectionTable(name = "FaceIds")
    List<String> faceId = new ArrayList<>();

    @OneToOne(mappedBy = "faceMeta", fetch = FetchType.LAZY)
    @JsonIgnore
    User user;


}
