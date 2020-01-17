package com.facevisitor.api.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;
}
