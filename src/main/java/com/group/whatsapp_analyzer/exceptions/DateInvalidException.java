package com.group.whatsapp_analyzer.exceptions;

public class DateInvalidException extends RuntimeException {
    public DateInvalidException(String message) {
        super(message);
    }
    public DateInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
