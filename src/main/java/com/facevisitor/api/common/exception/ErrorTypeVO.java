package com.facevisitor.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class ErrorTypeVO implements Serializable {

    private static final long serialVersionUID = -4338435841861016833L;

    private int status;

    private String message;
}

