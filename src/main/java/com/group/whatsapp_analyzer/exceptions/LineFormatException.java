package com.group.whatsapp_analyzer.exceptions;

public class LineFormatException extends RuntimeException {
    public LineFormatException(String message) {
        super(message);
    }
    public LineFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
