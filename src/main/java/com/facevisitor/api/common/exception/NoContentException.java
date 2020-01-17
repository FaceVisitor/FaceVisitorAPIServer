package com.facevisitor.api.common.exception;

/**
 * 204 No Content(성공이지만 내용이 없음.)
 */

public class NoContentException extends RuntimeException {

    private static final long serialVersionUID = 2562710582355164027L;

    public NoContentException() {
        super("NO_CONTENT");
    }

    public NoContentException(String message) {
        super(message);
    }
}
