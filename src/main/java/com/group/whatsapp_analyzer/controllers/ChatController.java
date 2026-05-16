package com.group.whatsapp_analyzer.controllers;

import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.ParseResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.group.whatsapp_analyzer.services.ParserService;
import com.group.whatsapp_analyzer.services.EstadisticaService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ParserService parserService;
    private final EstadisticaService estadisticaService;


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
        ChatDataSet chatDataSet = parserService.postProcesar(resultado);

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("mensajesPorUsuario", estadisticaService.contarMensajesPorUsuario(chatDataSet));
        estadisticas.put("frecuenciaPorHora", estadisticaService.obtenerFrecuenciaMensajesPorHora(chatDataSet));
        estadisticas.put("horaMayorActividad", estadisticaService.obtenerHoraMayorActividad(chatDataSet));
        estadisticas.put("palabrasFrecuentes", estadisticaService.contarFrecuencia(chatDataSet.getMensajes()));

        List<String> emojis = estadisticaService.extraerEmojis(chatDataSet);
        estadisticas.put("frecuenciaEmojis", estadisticaService.contarFrecuenciaEmojis(emojis));
        estadisticas.put("emojiMasUtilizado", estadisticaService.obtenerEmojiMasUtilizado(estadisticaService.contarFrecuenciaEmojis(emojis)));
       
        estadisticas.put("mensajesPorDia", estadisticaService.contarMensajesPorDia(chatDataSet.getMensajes()));
        estadisticas.put("diasMasActivos", estadisticaService.obtenerDiasMasActivos(chatDataSet.getMensajes()));

        return ResponseEntity.ok(estadisticas);
    }
}