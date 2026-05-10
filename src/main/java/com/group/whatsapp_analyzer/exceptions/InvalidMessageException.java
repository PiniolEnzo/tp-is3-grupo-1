package com.group.whatsapp_analyzer.exceptions;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String message) {
        super(message);
    }
    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
