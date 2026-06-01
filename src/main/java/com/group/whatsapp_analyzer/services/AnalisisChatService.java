package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.dto.ActividadUsuarioDTO;
import com.group.whatsapp_analyzer.dto.EstadisticasNormalizadasDTO;
import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import com.group.whatsapp_analyzer.logger.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalisisChatService {

    private final ParserService parserService;
    private final EstadisticaService estadisticaService;
    private final ConsolaService consolaService;

    public EstadisticasNormalizadasDTO analizar(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            Logger.getInstance().log("INFO", "Starting chat processing pipeline for file: " + fileName, fileName);

            ChatDataSet dataSet = parserService.postProcesar(parserService.leerArchivo(file), fileName);
            Logger.getInstance().log("INFO", "Parsing stage completed for file: " + fileName, fileName);

            List<Mensaje> mensajes = dataSet.getMensajes();
            ActividadUsuarioDTO actividadUsuario = estadisticaService.contarMensajesPorUsuario(dataSet);
            List<String> emojis = estadisticaService.extraerEmojis(dataSet);
            Logger.getInstance().log("INFO", "Initial statistics calculation completed for file: " + fileName, fileName);

            EstadisticasNormalizadasDTO estadisticas = EstadisticasNormalizadasDTO.builder()
                    .mensajesPorUsuario(actividadUsuario.getMensajesPorUsuario())
                    .usuarioConMasMensajes(actividadUsuario.getUsuarioConMasMensajes())
                    .cantidadMensajesUsuarioMasActivo(actividadUsuario.getCantidadMensajesUsuarioMasActivo())
                    .frecuenciaPalabras(estadisticaService.contarFrecuencia(mensajes))
                    .frecuenciaEmojis(estadisticaService.contarFrecuenciaEmojis(emojis))
                    .emojiMasUtilizado(estadisticaService.obtenerEmojiMasUtilizado(estadisticaService.contarFrecuenciaEmojis(emojis)))
                    .mensajesPorDia(estadisticaService.contarMensajesPorDia(mensajes))
                    .diasMasActivos(estadisticaService.obtenerDiasMasActivos(mensajes))
                    .mensajesPorHora(estadisticaService.obtenerFrecuenciaMensajesPorHora(dataSet))
                    .horaMasActiva(estadisticaService.obtenerHoraMayorActividad(dataSet))
                    .build();
            Logger.getInstance().log("INFO", "Full statistics aggregation completed for file: " + fileName, fileName);

            consolaService.mostrarResultados(estadisticas);
            Logger.getInstance().log("INFO", "Chat processing pipeline completed successfully for file: " + fileName, fileName);
            return estadisticas;
        } catch (Exception e) {
            Logger.getInstance().log("ERROR", "Pipeline failed for file " + fileName + ": " + e.getMessage(), fileName);
            throw e;
        }
    }

}
