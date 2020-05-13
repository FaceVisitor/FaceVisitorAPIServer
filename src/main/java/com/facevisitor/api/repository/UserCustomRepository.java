package com.facevisitor.api.repository;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.UserDTO;

import java.util.List;

public interface UserCustomRepository {
    User getUserByFaceIDs(String faceID);

    List<UserDTO.UserListResponse> getUserListByStoreId(Long storeId);
}
