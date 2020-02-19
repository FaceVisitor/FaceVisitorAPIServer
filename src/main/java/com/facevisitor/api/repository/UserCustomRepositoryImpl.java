package com.facevisitor.api.repository;

import com.facevisitor.api.domain.face.QFaceMeta;
import com.facevisitor.api.domain.user.QUser;
import com.facevisitor.api.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserCustomRepositoryImpl extends QuerydslRepositorySupport implements UserCustomRepository {

    QUser qUser = QUser.user;
    QFaceMeta qFaceMeta = QFaceMeta.faceMeta;

    JPAQueryFactory jpaQueryFactory;

    public UserCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.jpaQueryFactory = queryFactory;
    }

    @Override
    public User getUserByFaceIDs(String faceId){
//        return jpaQueryFactory.selectFrom(qUser).innerJoin(qUser.faceMeta,qFaceMeta).join(qFaceMeta.faceId).where(qFaceMeta.faceId.contains(faceId)).fetchOne();
        return null;
    }

}
