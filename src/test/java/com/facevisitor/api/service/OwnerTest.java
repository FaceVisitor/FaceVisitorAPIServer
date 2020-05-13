package com.facevisitor.api.service;

import com.facevisitor.api.dto.order.OrderDTO;
import com.facevisitor.api.dto.user.UserDTO;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.repository.UserToStoreRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OwnerTest {

    @Autowired
    UserToStoreRepository userToStoreRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 상점에_가입한_유저찾기() {
        Long storeId = 1L;
        List<UserDTO.UserListResponse> usersByStoreId = userRepository.getUserListByStoreId(storeId);
        System.out.println(usersByStoreId);
    }

    @Test
    public void 해당_상점에_유저의_주문목록() {
        List<OrderDTO.OrderListResponseItem> orderByUserAndStore = userRepository.getOrderListByStore(1L);
        System.out.println(orderByUserAndStore);
    }
}
