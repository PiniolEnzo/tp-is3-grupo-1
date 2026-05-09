package com.group.whatsapp_analyzer.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<String> handleInvalidFile(InvalidFileException e) {
        return ResponseEntity.badRequest().body("Error al leer el archivo: " + e.getMessage());
    }

    @ExceptionHandler(DateInvalidException.class)
    public ResponseEntity<String> handleDateInvalid(DateInvalidException e) {
        return ResponseEntity.badRequest().body("Error en el formato de la fecha del chat: " + e.getMessage());
    }

    @ExceptionHandler(TimeInvalidException.class)
    public ResponseEntity<String> handleTimeInvalid(TimeInvalidException e) {
        return ResponseEntity.badRequest().body("Error en el formato de la hora del chat: " + e.getMessage());
    }

    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<String> handleInvalidMessage(InvalidMessageException e) {
        return ResponseEntity.badRequest().body("Error en el mensaje: " + e.getMessage());
    }

    @ExceptionHandler(LineFormatException.class)
    public ResponseEntity<String> handleLineFormat(LineFormatException e) {
        return ResponseEntity.badRequest().body("Error de formato: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception e) {
        return ResponseEntity.internalServerError().body("Error interno del servidor: " + e.getMessage());
    }
}