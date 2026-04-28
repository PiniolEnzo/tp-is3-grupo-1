package com.group.whatsapp_analyzer.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.group.whatsapp_analyzer.services.ParserService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ParserService parserService;

    public ChatController(ParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadChat(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!file.getOriginalFilename().endsWith(".txt")) {
            return ResponseEntity.badRequest().body("El archivo debe ser un .txt");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("El archivo es demasiado grande. Máximo 10MB");
        }

        try {
            List<String> lineas = parserService.leerArchivo(file);
            return ResponseEntity.ok("Archivo leído correctamente. Total de líneas: " + lineas.size());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al leer el archivo: " + e.getMessage());
        }
    }
}