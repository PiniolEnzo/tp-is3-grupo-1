package com.group.whatsapp_analyzer.exceptions;

public class TimeInvalidException extends RuntimeException {
    public TimeInvalidException(String message) {
        super(message);
    }
    public TimeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
