package com.facevisitor.api.domain.store;

import com.facevisitor.api.domain.base.BaseImageFileEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString(exclude = {"store"})
public class StoreImage extends BaseImageFileEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Store store;

}
