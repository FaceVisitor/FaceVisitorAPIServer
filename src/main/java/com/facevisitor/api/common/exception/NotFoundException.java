package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * 404 Not Found(찾을 수 없음)
 * 클라이언트가 요청한 자원에 서버에 없다는 것을 나타냅니다.
 * 서버가 Request-URI와 일치하는 것을 아무것도 발견하지 못했다는 의미로 이러한 상태가 잠정적인지 영구적인지 관한 아무런 표시도 주어지지 않은 경우입니다.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements Serializable {

    public NotFoundException() {
        super("해당 데이터를 찾을 수 없습니다.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
