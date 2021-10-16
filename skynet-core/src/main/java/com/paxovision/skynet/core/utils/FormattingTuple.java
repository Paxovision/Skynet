package com.paxovision.skynet.core.utils;

public class FormattingTuple {
    private final String message;
    private final Throwable throwable;
    private final Object[] argArray;

    public FormattingTuple(String message) {
        this(message, (Object[])null, (Throwable)null);
    }

    public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
        this.argArray = argArray;
    }

    public String getMessage() {
        return this.message;
    }

    public Object[] getArgArray() {
        return this.argArray;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}

