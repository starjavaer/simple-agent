package com.ixingji.agent.guarder.exception;

public class ServerJarNotFoundException extends Exception {

    public ServerJarNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServerJarNotFoundException(String message) {
        super(message);
    }

}
