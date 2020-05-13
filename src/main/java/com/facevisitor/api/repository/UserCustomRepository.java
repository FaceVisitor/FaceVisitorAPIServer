package com.facevisitor.api.repository;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.dto.user.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface UserCustomRepository {
    User getUserByFaceIDs(String faceID);

    List<UserDTO.UserListResponse> getUserListByStoreId(Long storeId);

    List<OrderDTO.OrderListResponseItem> getOrderListByStore(Long storeId);

    List<OrderDTO.OrderListResponseItem> getOrderListByStoreAndUser(Long storeId, Long userId);

    BigDecimal getIncome(Long storeId);
}
