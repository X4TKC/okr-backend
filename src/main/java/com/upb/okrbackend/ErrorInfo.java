package com.upb.okrbackend;

public class ErrorInfo {
    private final int code;
    private final String description;

    public ErrorInfo(int code, String description) {
        this.code = code;
        this.description = description;
    }
}