package com.facevisitor.api.repository;

import com.facevisitor.api.domain.user.User;

public interface UserCustomRepository {
    User getUserByFaceIDs(String faceID);
}
