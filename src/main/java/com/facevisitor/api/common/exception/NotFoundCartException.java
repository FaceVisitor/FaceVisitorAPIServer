package com.facevisitor.api.common.exception;

public class NotFoundCartException extends BadRequestException{
    public NotFoundCartException() {
        super("장바구니를 찾울 수 없습니다.");
    }

    public NotFoundCartException(Long id) {
        super("id :" + id + ", 해당 장바구니를 찾울 수 없습니다.");
    }
}
