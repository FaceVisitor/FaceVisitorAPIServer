package com.facevisitor.api.domain.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY) // 모든 필드에 json 적용
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public abstract class BaseImageFileEntity implements Serializable {

    @Getter
    @Setter
    String url;

    @Getter
    @Setter
    String name = "";

    // 생성한 날짜
    @Getter
    @Setter
    @CreatedDate
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    protected LocalDateTime createdAt;

    // 생성한 사용자 아이디
    @JsonIgnore
    @CreatedBy
    protected String createdBy;

    // 업데이트한 날짜
    @JsonIgnore
    @Getter
    @LastModifiedDate
    @Setter
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    protected LocalDateTime updatedAt;

    // 업데이트한 사용자 아이디
    @JsonIgnore
    @LastModifiedBy
    protected String updatedBy;

    // 버전관리 트렉젝션이 동작할때마다 버전이 +1이 업데이트 된다. ORM책 p.689 참고
    @Getter
    @Version
    @Column(columnDefinition = "BIGINT(20) default 0")
    @JsonIgnore
    protected Long version;
}
