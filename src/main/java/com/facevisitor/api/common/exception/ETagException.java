package com.facevisitor.api.common.exception;

public class ETagException extends RuntimeException {
    public ETagException() {
        super("버전이 맞지 않습니다.");
    }

    public ETagException(String message) {
        super(message);
    }
}
