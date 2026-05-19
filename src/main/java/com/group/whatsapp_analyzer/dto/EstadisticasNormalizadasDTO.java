package com.group.whatsapp_analyzer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
public class EstadisticasNormalizadasDTO {
    private Map<String, Integer> mensajesPorUsuario;
    private String usuarioConMasMensajes;
    private Integer cantidadMensajesUsuarioMasActivo;
    private Map<String, Integer> frecuenciaPalabras;
    private Map<String, Integer> frecuenciaEmojis;
    private String emojiMasUtilizado;
    private Map<String, Integer> mensajesPorDia;
    private List<String> diasMasActivos;
    private Map<Integer, Integer> mensajesPorHora;
    private Integer horaMasActiva;
}
