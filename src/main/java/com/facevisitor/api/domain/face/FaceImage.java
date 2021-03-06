package com.facevisitor.api.domain.face;

import com.facevisitor.api.domain.base.BaseImageFileEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = "faceMeta")
public class FaceImage extends BaseImageFileEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    FaceMeta faceMeta;
}
