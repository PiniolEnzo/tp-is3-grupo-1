package com.group.whatsapp_analyzer.controllers;

import com.group.whatsapp_analyzer.services.AnalisisChatService;
import com.group.whatsapp_analyzer.logger.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AnalisisChatService analisisChatService;

    public ChatController(AnalisisChatService analisisChatService) {
        this.analisisChatService = analisisChatService;
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadChat(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        long startTime = System.currentTimeMillis();
        Logger.getInstance().log("INFO", "Chat upload request received. File: " + fileName, fileName);

        if (file.isEmpty()) {
            Logger.getInstance().log("WARN", "Chat upload failed: File is empty", fileName);
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!fileName.endsWith(".txt")) {
            Logger.getInstance().log("WARN", "Chat upload failed: Invalid extension. File: " + fileName, fileName);
            return ResponseEntity.badRequest().body("El archivo debe ser un .txt");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            Logger.getInstance().log("WARN", "Chat upload failed: File too large. Name: " + fileName, fileName);
            return ResponseEntity.badRequest().body("El archivo es demasiado grande. Máximo 10MB");
        }

        try {
            ResponseEntity<?> response = ResponseEntity.ok(analisisChatService.analizar(file));
            long duration = System.currentTimeMillis() - startTime;
            Logger.getInstance().log("INFO", "Chat upload processed successfully. File: " + fileName + ". Duration: " + duration + "ms", fileName);
            return response;
        } catch (Exception e) {
            Logger.getInstance().log("ERROR", "Critical error processing chat file " + fileName + ": " + e.getMessage(), fileName);
            return ResponseEntity.internalServerError().body("Error interno al procesar el chat: " + e.getMessage());
        }
    }
}