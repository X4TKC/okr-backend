package com.upb.okrbackend;

public class OkrExceptionErrors extends Exception{
    public OkrExceptionErrors(String message) {
        super(message);
    }

    public OkrExceptionErrors() {
    }

    public OkrExceptionErrors(String message, Throwable cause) {
        super(message, cause);
    }

    public OkrExceptionErrors(Throwable cause) {
        super(cause);
    }

    public OkrExceptionErrors(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
