package com.facevisitor.api.repository;

import com.facevisitor.api.domain.face.QFaceMeta;
import com.facevisitor.api.domain.order.FVOrder;
import com.facevisitor.api.domain.order.QFVOrder;
import com.facevisitor.api.domain.order.QOrderLineItem;
import com.facevisitor.api.domain.user.QUser;
import com.facevisitor.api.domain.user.QUserToStore;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.dto.user.UserDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserCustomRepositoryImpl extends QuerydslRepositorySupport implements UserCustomRepository {


    ModelMapper modelMapper;
    QUser qUser = QUser.user;
    QFaceMeta qFaceMeta = QFaceMeta.faceMeta;
    QUserToStore qUserToStore = QUserToStore.userToStore;
    QFVOrder qOrder = QFVOrder.fVOrder;
    QOrderLineItem qOrderLineItem = QOrderLineItem.orderLineItem;

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
        return jpaQueryFactory.select(qUserToStore.user).from(qUserToStore)
                .leftJoin(qUserToStore.user, qUser).leftJoin(qUser.faceMeta)
                .orderBy(qUser.createdAt.desc())
                .where(qUserToStore.store.id.eq(storeId))
                .fetch().stream()
                .map(result -> modelMapper.map(result, UserDTO.UserListResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO.OrderListResponseItem> getOrderListByStore(Long storeId) {
        return jpaQueryFactory.select(qOrder).from(qOrder)
                .leftJoin(qOrder.lineItems, qOrderLineItem)
                .fetchJoin()
                .leftJoin(qOrderLineItem.goods)
                .fetchJoin()
                .orderBy(qOrder.createdAt.desc())
                .where(qOrder.store.id.eq(storeId))
                .fetch()
                .stream().filter(Objects::nonNull)
                .map(fvOrder -> modelMapper.map(fvOrder, OrderDTO.OrderListResponseItem.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO.OrderListResponseItem> getOrderListByStoreAndUser(Long storeId, Long userId) {
        return jpaQueryFactory.select(qOrder).from(qUserToStore)
                .leftJoin(qUserToStore.user, qUser)
                .leftJoin(qUser.orders, qOrder)
                .leftJoin(qOrder.lineItems)
                .fetchJoin()
                .distinct()
                .orderBy(qOrder.createdAt.desc())
                .where(qUserToStore.store.id.eq(storeId))
                .where(qUser.id.eq(userId))
                .fetch()
                .stream().map(fvOrder -> modelMapper.map(fvOrder, OrderDTO.OrderListResponseItem.class)).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getIncome(Long storeId) {
        List<FVOrder> collect = jpaQueryFactory.select(qOrder).from(qOrder)
                .leftJoin(qOrder.lineItems, qOrderLineItem)
                .fetchJoin()
                .leftJoin(qOrderLineItem.goods)
                .fetchJoin()
                .where(qOrder.store.id.eq(storeId))
                .orderBy(qOrder.createdAt.desc())
                .fetch()
                .stream().filter(Objects::nonNull)
                .collect(Collectors.toList());
        return BigDecimal.valueOf(collect.stream().mapToDouble(result -> Double.parseDouble(result.getPayPrice().toString())).sum());
    }

}
