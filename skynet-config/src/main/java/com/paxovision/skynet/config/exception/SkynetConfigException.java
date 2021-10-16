package com.paxovision.skynet.config.exception;

public class SkynetConfigException extends RuntimeException{
    private static final long serialVersionUID = 2402576231289911388L;
    public SkynetConfigException() {
        super();
    }

    public SkynetConfigException(String message) {
        super(message);
    }

    public SkynetConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkynetConfigException(Throwable cause) {
        super(cause);
    }
}
