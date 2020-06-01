package com.ixingji.agent.guarder.exception;

public class AuthCheckFailedException extends RuntimeException {

    public AuthCheckFailedException(String message) {
        super(message);
    }

    public AuthCheckFailedException(Throwable cause) {
        super(cause);
    }

}
