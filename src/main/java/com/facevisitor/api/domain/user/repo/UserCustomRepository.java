package com.facevisitor.api.domain.user.repo;

import com.facevisitor.api.domain.user.User;

public interface UserCustomRepository {
    User getUserByFaceIDs(String faceID);
}
