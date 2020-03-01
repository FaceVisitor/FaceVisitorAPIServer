package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotFoundGoodsException extends NotFoundException implements Serializable {

    private static final long serialVersionUID = 8087908946312578899L;

    public NotFoundGoodsException() {
        super("해당 상품을 찾울 수 없습니다.");
    }

    public NotFoundGoodsException(Long id) {
        super("id :" + id + ", 해당상품을 찾울 수 없습니다.");
    }
}

