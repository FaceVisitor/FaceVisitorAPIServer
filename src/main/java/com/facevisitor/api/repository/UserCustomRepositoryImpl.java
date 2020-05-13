package com.facevisitor.api.repository;

import com.facevisitor.api.domain.face.QFaceMeta;
import com.facevisitor.api.domain.user.QUser;
import com.facevisitor.api.domain.user.QUserToStore;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.UserDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class UserCustomRepositoryImpl extends QuerydslRepositorySupport implements UserCustomRepository {


    ModelMapper modelMapper;
    QUser qUser = QUser.user;
    QFaceMeta qFaceMeta = QFaceMeta.faceMeta;
    QUserToStore qUserToStore = QUserToStore.userToStore;

    JPAQueryFactory jpaQueryFactory;

    public UserCustomRepositoryImpl(JPAQueryFactory queryFactory, ModelMapper modelMapper) {
        super(User.class);
        this.jpaQueryFactory = queryFactory;
        this.modelMapper = modelMapper;
    }

    @Override
    public User getUserByFaceIDs(String faceId) {
//        return jpaQueryFactory.selectFrom(qUser).innerJoin(qUser.faceMeta,qFaceMeta).join(qFaceMeta.faceId).where(qFaceMeta.faceId.contains(faceId)).fetchOne();
        return null;
    }

    @Override
    public List<UserDTO.UserListResponse> getUserListByStoreId(Long storeId) {
        List<UserDTO.UserListResponse> userListResponses =
                jpaQueryFactory.select(qUserToStore.user).from(qUserToStore)
                .leftJoin(qUserToStore.user,qUser).leftJoin(qUser.faceMeta)
                .where(qUserToStore.store.id.eq(storeId))
                .fetch().stream()
                .map(result -> modelMapper.map(result, UserDTO.UserListResponse.class))
                .collect(Collectors.toList());

        System.out.println(userListResponses);
        return userListResponses;
    }

}
