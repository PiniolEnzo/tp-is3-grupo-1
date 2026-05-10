package com.group.whatsapp_analyzer.exceptions;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String message) {
        super(message);
    }
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
