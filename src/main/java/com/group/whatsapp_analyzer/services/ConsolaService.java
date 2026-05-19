package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.dto.EstadisticasNormalizadasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsolaService {

    private static final Logger log = LoggerFactory.getLogger(ConsolaService.class);

    public void mostrarResultados(EstadisticasNormalizadasDTO estadisticas) {

        log.info("=== RESULTADOS DEL ANÁLISIS ===");

        // Usuario con más mensajes
        log.info("👤 Más mensajes: {} con {} mensajes",
                estadisticas.getUsuarioConMasMensajes(),
                estadisticas.getCantidadMensajesUsuarioMasActivo());

        // Emoji más usado
        log.info("😂 Emoji top: {} -> {} veces",
                estadisticas.getEmojiMasUtilizado(),
                estadisticas.getFrecuenciaEmojis() != null ?
                estadisticas.getFrecuenciaEmojis().get(estadisticas.getEmojiMasUtilizado()) : 0);

        // Franja horaria más activa
        log.info("⏰ Franja más activa: {}hs con {} mensajes",
                estadisticas.getHoraMasActiva(),
                estadisticas.getMensajesPorHora() != null ?
                estadisticas.getMensajesPorHora().get(estadisticas.getHoraMasActiva()) : 0);

        // Días más activos
        log.info("📅 Días con más mensajes:");
        if (estadisticas.getDiasMasActivos() != null) {
            for (String dia : estadisticas.getDiasMasActivos()) {
                log.info("   - {} con {} mensajes", dia,
                        estadisticas.getMensajesPorDia().get(dia));
            }
        }

        // Top 10 palabras frecuentes
        log.info("☁️ Top 10 palabras más frecuentes:");
        if (estadisticas.getFrecuenciaPalabras() != null) {
            estadisticas.getFrecuenciaPalabras().entrySet().stream()
                    .limit(10)
                    .forEach(entry -> log.info("   - {}: {} veces", entry.getKey(), entry.getValue()));
        }

        log.info("=== FIN DEL ANÁLISIS ===");
    }
}