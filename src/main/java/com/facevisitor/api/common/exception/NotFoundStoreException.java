package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotFoundStoreException extends NotFoundException implements Serializable {

    private static final long serialVersionUID = 8087908946312578899L;

    public NotFoundStoreException() {
        super("해당 매장을 찾울 수 없습니다.");
    }

    public NotFoundStoreException(Long id) {
        super("id :" + id + ", 해당매장을 찾울 수 없습니다.");
    }
}

