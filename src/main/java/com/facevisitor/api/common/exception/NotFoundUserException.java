package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotFoundUserException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 8087908946312578899L;

    public NotFoundUserException() {
        super("로그인 정보를 확인해주세요");
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}

