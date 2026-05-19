package com.group.whatsapp_analyzer.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public EstadisticasNormalizadasDTO() {}

    public EstadisticasNormalizadasDTO(Map<String, Integer> mensajesPorUsuario, String usuarioConMasMensajes, Integer cantidadMensajesUsuarioMasActivo, Map<String, Integer> frecuenciaPalabras, Map<String, Integer> frecuenciaEmojis, String emojiMasUtilizado, Map<String, Integer> mensajesPorDia, List<String> diasMasActivos, Map<Integer, Integer> mensajesPorHora, Integer horaMasActiva) {
        this.mensajesPorUsuario = mensajesPorUsuario;
        this.usuarioConMasMensajes = usuarioConMasMensajes;
        this.cantidadMensajesUsuarioMasActivo = cantidadMensajesUsuarioMasActivo;
        this.frecuenciaPalabras = frecuenciaPalabras;
        this.frecuenciaEmojis = frecuenciaEmojis;
        this.emojiMasUtilizado = emojiMasUtilizado;
        this.mensajesPorDia = mensajesPorDia;
        this.diasMasActivos = diasMasActivos;
        this.mensajesPorHora = mensajesPorHora;
        this.horaMasActiva = horaMasActiva;
    }

    public Map<String, Integer> getMensajesPorUsuario() { return mensajesPorUsuario; }
    public void setMensajesPorUsuario(Map<String, Integer> mensajesPorUsuario) { this.mensajesPorUsuario = mensajesPorUsuario; }

    public String getUsuarioConMasMensajes() { return usuarioConMasMensajes; }
    public void setUsuarioConMasMensajes(String usuarioConMasMensajes) { this.usuarioConMasMensajes = usuarioConMasMensajes; }

    public Integer getCantidadMensajesUsuarioMasActivo() { return cantidadMensajesUsuarioMasActivo; }
    public void setCantidadMensajesUsuarioMasActivo(Integer cantidadMensajesUsuarioMasActivo) { this.cantidadMensajesUsuarioMasActivo = cantidadMensajesUsuarioMasActivo; }

    public Map<String, Integer> getFrecuenciaPalabras() { return frecuenciaPalabras; }
    public void setFrecuenciaPalabras(Map<String, Integer> frecuenciaPalabras) { this.frecuenciaPalabras = frecuenciaPalabras; }

    public Map<String, Integer> getFrecuenciaEmojis() { return frecuenciaEmojis; }
    public void setFrecuenciaEmojis(Map<String, Integer> frecuenciaEmojis) { this.frecuenciaEmojis = frecuenciaEmojis; }

    public String getEmojiMasUtilizado() { return emojiMasUtilizado; }
    public void setEmojiMasUtilizado(String emojiMasUtilizado) { this.emojiMasUtilizado = emojiMasUtilizado; }

    public Map<String, Integer> getMensajesPorDia() { return mensajesPorDia; }
    public void setMensajesPorDia(Map<String, Integer> mensajesPorDia) { this.mensajesPorDia = mensajesPorDia; }

    public List<String> getDiasMasActivos() { return diasMasActivos; }
    public void setDiasMasActivos(List<String> diasMasActivos) { this.diasMasActivos = diasMasActivos; }

    public Map<Integer, Integer> getMensajesPorHora() { return mensajesPorHora; }
    public void setMensajesPorHora(Map<Integer, Integer> mensajesPorHora) { this.mensajesPorHora = mensajesPorHora; }

    public Integer getHoraMasActiva() { return horaMasActiva; }
    public void setHoraMasActiva(Integer horaMasActiva) { this.horaMasActiva = horaMasActiva; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public Builder mensajesPorUsuario(Map<String, Integer> mensajesPorUsuario) { this.mensajesPorUsuario = mensajesPorUsuario; return this; }
        public Builder usuarioConMasMensajes(String usuarioConMasMensajes) { this.usuarioConMasMensajes = usuarioConMasMensajes; return this; }
        public Builder cantidadMensajesUsuarioMasActivo(Integer cantidadMensajesUsuarioMasActivo) { this.cantidadMensajesUsuarioMasActivo = cantidadMensajesUsuarioMasActivo; return this; }
        public Builder frecuenciaPalabras(Map<String, Integer> frecuenciaPalabras) { this.frecuenciaPalabras = frecuenciaPalabras; return this; }
        public Builder frecuenciaEmojis(Map<String, Integer> frecuenciaEmojis) { this.frecuenciaEmojis = frecuenciaEmojis; return this; }
        public Builder emojiMasUtilizado(String emojiMasUtilizado) { this.emojiMasUtilizado = emojiMasUtilizado; return this; }
        public Builder mensajesPorDia(Map<String, Integer> mensajesPorDia) { this.mensajesPorDia = mensajesPorDia; return this; }
        public Builder diasMasActivos(List<String> diasMasActivos) { this.diasMasActivos = diasMasActivos; return this; }
        public Builder mensajesPorHora(Map<Integer, Integer> mensajesPorHora) { this.mensajesPorHora = mensajesPorHora; return this; }
        public Builder horaMasActiva(Integer horaMasActiva) { this.horaMasActiva = horaMasActiva; return this; }

        public EstadisticasNormalizadasDTO build() {
            return new EstadisticasNormalizadasDTO(mensajesPorUsuario, usuarioConMasMensajes, cantidadMensajesUsuarioMasActivo, frecuenciaPalabras, frecuenciaEmojis, emojiMasUtilizado, mensajesPorDia, diasMasActivos, mensajesPorHora, horaMasActiva);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadisticasNormalizadasDTO that = (EstadisticasNormalizadasDTO) o;
        return Objects.equals(mensajesPorUsuario, that.mensajesPorUsuario) &&
               Objects.equals(usuarioConMasMensajes, that.usuarioConMasMensajes) &&
               Objects.equals(cantidadMensajesUsuarioMasActivo, that.cantidadMensajesUsuarioMasActivo) &&
               Objects.equals(frecuenciaPalabras, that.frecuenciaPalabras) &&
               Objects.equals(frecuenciaEmojis, that.frecuenciaEmojis) &&
               Objects.equals(emojiMasUtilizado, that.emojiMasUtilizado) &&
               Objects.equals(mensajesPorDia, that.mensajesPorDia) &&
               Objects.equals(diasMasActivos, that.diasMasActivos) &&
               Objects.equals(mensajesPorHora, that.mensajesPorHora) &&
               Objects.equals(horaMasActiva, that.horaMasActiva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensajesPorUsuario, usuarioConMasMensajes, cantidadMensajesUsuarioMasActivo, frecuenciaPalabras, frecuenciaEmojis, emojiMasUtilizado, mensajesPorDia, diasMasActivos, mensajesPorHora, horaMasActiva);
    }

    @Override
    public String toString() {
        return "EstadisticasNormalizadasDTO{" +
               "mensajesPorUsuario=" + mensajesPorUsuario +
               ", usuarioConMasMensajes='" + usuarioConMasMensajes + '\'' +
               ", cantidadMensajesUsuarioMasActivo=" + cantidadMensajesUsuarioMasActivo +
               ", frecuenciaPalabras=" + frecuenciaPalabras +
               ", frecuenciaEmojis=" + frecuenciaEmojis +
               ", emojiMasUtilizado='" + emojiMasUtilizado + '\'' +
               ", mensajesPorDia=" + mensajesPorDia +
               ", diasMasActivos=" + diasMasActivos +
               ", mensajesPorHora=" + mensajesPorHora +
               ", horaMasActiva=" + horaMasActiva +
               '}';
    }
}
