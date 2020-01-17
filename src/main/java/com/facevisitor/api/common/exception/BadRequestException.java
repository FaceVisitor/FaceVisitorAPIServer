package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * 400 Bad Request(잘못된 요구)
 * 클라이언트의 요청에 문법적인 오류가 있다는 것을 서버가 알아냈다는 것을 의미합니다.
 * 잘못된 형식 때문에 서버가 요구를 이해할 수 없습니다. 클라이언트는 변경 없이 요구를 반복해서는 안 됩니다.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 8087908946312578899L;

    public BadRequestException() {
        super("잘못된 요청입니다.");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
