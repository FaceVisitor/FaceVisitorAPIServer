package com.facevisitor.api.domain.user;


import com.facevisitor.api.domain.base.BaseEntity;
import com.facevisitor.api.domain.store.Store;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Slf4j
@Entity(name = "UserToStore")
@Getter
@Setter
public class UserToStore extends BaseEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    User user;

    @ManyToOne
    Store store;


}
