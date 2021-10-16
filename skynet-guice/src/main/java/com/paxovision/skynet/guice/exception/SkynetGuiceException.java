package com.paxovision.skynet.guice.exception;

public class SkynetGuiceException extends RuntimeException{
    private static final long serialVersionUID = 2402576231289911388L;
    public SkynetGuiceException() {
        super();
    }

    public SkynetGuiceException(String message) {
        super(message);
    }

    public SkynetGuiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkynetGuiceException(Throwable cause) {
        super(cause);
    }
}
