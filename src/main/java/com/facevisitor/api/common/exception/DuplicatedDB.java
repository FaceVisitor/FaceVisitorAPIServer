package com.facevisitor.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedDB extends RuntimeException {

    private static final long serialVersionUID = 8087908946312578899L;

    public DuplicatedDB() {
        super("기존에 중복되는 정보가 있습니다.");
    }

    public DuplicatedDB(String message) {
        super(message);
    }
}
