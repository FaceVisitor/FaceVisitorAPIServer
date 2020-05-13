package com.facevisitor.api.owner.service;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.dto.user.UserDTO;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.repository.UserToStoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ManageService {

    UserToStoreRepository userToStoreRepository;

    UserRepository userRepository;

    ModelMapper modelMapper;


    public List<UserDTO.UserListResponse> findUsersByStoreId(Long storeId) {
        return userRepository.getUserListByStoreId(storeId);
    }


    public OrderDTO.OrderListResponse findOrdersByStore(Long storeId) {
        List<OrderDTO.OrderListResponseItem> orderListByStore = userRepository.getOrderListByStore(storeId);
        OrderDTO.OrderListResponse response = new OrderDTO.OrderListResponse();
        response.setOrders(orderListByStore);
        response.setTotalPrice(userRepository.getIncome(storeId));
        return response;
    }

    public List<OrderDTO.OrderListResponseItem> findOrdersByStoreAndUser(Long storeId, Long userId) {
        return userRepository.getOrderListByStoreAndUser(storeId, userId);
    }

    public UserDTO.UserDetailResponse getUserById(Long id) {
        return modelMapper.map(userRepository.getById(id).orElseThrow(NotFoundException::new), UserDTO.UserDetailResponse.class);
    }


}
