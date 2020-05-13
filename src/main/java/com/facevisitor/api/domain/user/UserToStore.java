package com.facevisitor.api.domain.user;


import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.store.Store;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity(name = "UserToStore")
@Getter
@Setter
public class UserToStore extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    Store store;


}
