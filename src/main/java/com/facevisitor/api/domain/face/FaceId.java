package com.facevisitor.api.domain.face;


import com.facevisitor.api.domain.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "faceMeta")
public class FaceId extends BaseEntity {

    @Id
    String faceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    FaceMeta faceMeta;
}
