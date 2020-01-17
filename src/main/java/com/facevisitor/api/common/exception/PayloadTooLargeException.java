package com.facevisitor.api.common.exception;

public class PayloadTooLargeException extends RuntimeException {

    public static final String MESSAGE_TOO_LARGE_FILE = "파일이 너무 큽니다.";
    private static final long serialVersionUID = 3417477999028683608L;

    public PayloadTooLargeException() {
        super("요청한 값이 너무 큽니다.");
    }

    public PayloadTooLargeException(Integer limitSize) {
        super(String.format("요청한 값이 너무 큽니다. (%sMB 이하)", limitSize));
    }

    public PayloadTooLargeException(String message) {
        super(message);
    }
}
