package com.fastcampus.fcsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO : implement
@Getter
@AllArgsConstructor
public class SnsApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public SnsApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }
    @Override
    public String getMessage() {
        return String.format("%s. %s", errorCode.getMessage(), message);
    }

}
