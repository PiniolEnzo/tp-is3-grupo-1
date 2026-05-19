package com.group.whatsapp_analyzer.dto;

import java.util.Map;
import java.util.Objects;

public class ActividadUsuarioDTO {
    private final Map<String, Integer> mensajesPorUsuario;
    private final String usuarioConMasMensajes;
    private final Integer cantidadMensajesUsuarioMasActivo;

    public ActividadUsuarioDTO(Map<String, Integer> mensajesPorUsuario, String usuarioConMasMensajes, Integer cantidadMensajesUsuarioMasActivo) {
        this.mensajesPorUsuario = mensajesPorUsuario;
        this.usuarioConMasMensajes = usuarioConMasMensajes;
        this.cantidadMensajesUsuarioMasActivo = cantidadMensajesUsuarioMasActivo;
    }

    public Map<String, Integer> getMensajesPorUsuario() {
        return mensajesPorUsuario;
    }

    public String getUsuarioConMasMensajes() {
        return usuarioConMasMensajes;
    }

    public Integer getCantidadMensajesUsuarioMasActivo() {
        return cantidadMensajesUsuarioMasActivo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Integer> mensajesPorUsuario;
        private String usuarioConMasMensajes;
        private Integer cantidadMensajesUsuarioMasActivo;

        public Builder mensajesPorUsuario(Map<String, Integer> mensajesPorUsuario) {
            this.mensajesPorUsuario = mensajesPorUsuario;
            return this;
        }

        public Builder usuarioConMasMensajes(String usuarioConMasMensajes) {
            this.usuarioConMasMensajes = usuarioConMasMensajes;
            return this;
        }

        public Builder cantidadMensajesUsuarioMasActivo(Integer cantidadMensajesUsuarioMasActivo) {
            this.cantidadMensajesUsuarioMasActivo = cantidadMensajesUsuarioMasActivo;
            return this;
        }

        public ActividadUsuarioDTO build() {
            return new ActividadUsuarioDTO(mensajesPorUsuario, usuarioConMasMensajes, cantidadMensajesUsuarioMasActivo);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActividadUsuarioDTO that = (ActividadUsuarioDTO) o;
        return Objects.equals(mensajesPorUsuario, that.mensajesPorUsuario) &&
               Objects.equals(usuarioConMasMensajes, that.usuarioConMasMensajes) &&
               Objects.equals(cantidadMensajesUsuarioMasActivo, that.cantidadMensajesUsuarioMasActivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensajesPorUsuario, usuarioConMasMensajes, cantidadMensajesUsuarioMasActivo);
    }

    @Override
    public String toString() {
        return "ActividadUsuarioDTO{" +
               "mensajesPorUsuario=" + mensajesPorUsuario +
               ", usuarioConMasMensajes='" + usuarioConMasMensajes + '\'' +
               ", cantidadMensajesUsuarioMasActivo=" + cantidadMensajesUsuarioMasActivo +
               '}';
    }
}
