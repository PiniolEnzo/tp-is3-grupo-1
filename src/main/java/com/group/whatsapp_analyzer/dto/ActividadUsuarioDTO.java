package com.group.whatsapp_analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ActividadUsuarioDTO {
    private Map<String, Integer> mensajesPorUsuario;
    private String usuarioConMasMensajes;
    private Integer cantidadMensajesUsuarioMasActivo;
}
