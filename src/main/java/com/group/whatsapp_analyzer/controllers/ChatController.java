package com.group.whatsapp_analyzer.controllers;

import com.group.whatsapp_analyzer.model.ParseResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.group.whatsapp_analyzer.services.ParserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ParserService parserService;

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

        ParseResult resultado = parserService.leerArchivo(file);
        return ResponseEntity.ok(parserService.postProcesar(resultado));
    }
}