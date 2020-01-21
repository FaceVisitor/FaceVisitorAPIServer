package com.facevisitor.api.domain.security;

import com.facevisitor.api.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "auth_id")
    private Long id;

    private String role;


}
