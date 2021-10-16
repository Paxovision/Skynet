package com.paxovision.skynet.core.exception;

public class SkynetCoreException extends RuntimeException{
    private static final long serialVersionUID = 2402576231289911388L;
    public SkynetCoreException() {
        super();
    }

    public SkynetCoreException(String message) {
        super(message);
    }

    public SkynetCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkynetCoreException(Throwable cause) {
        super(cause);
    }
}
