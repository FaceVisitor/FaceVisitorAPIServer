package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = -1620942756190363170L;

    public UnAuthorizedException() {
        super("인증에 실패했습니다.");
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
