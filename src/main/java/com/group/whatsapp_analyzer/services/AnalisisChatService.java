package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.dto.ActividadUsuarioDTO;
import com.group.whatsapp_analyzer.dto.EstadisticasNormalizadasDTO;
import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalisisChatService {

    private final ParserService parserService;
    private final EstadisticaService estadisticaService;

    public EstadisticasNormalizadasDTO analizar(MultipartFile file) {
        ChatDataSet dataSet = parserService.postProcesar(parserService.leerArchivo(file));
        List<Mensaje> mensajes = dataSet.getMensajes();
        ActividadUsuarioDTO actividadUsuario = estadisticaService.contarMensajesPorUsuario(dataSet);
        List<String> emojis = estadisticaService.extraerEmojis(dataSet);



        return EstadisticasNormalizadasDTO.builder()
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

    }
}
