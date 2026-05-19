package com.group.whatsapp_analyzer.model;

import java.util.Objects;

public class Mensaje {
    private String fecha;
    private String hora;
    private String usuario;
    private String contenido;
    private String fechaNormalizada;
    private MessageType tipoMensaje;
    private Integer longitudMensaje;
    private Integer cantidadPalabras;
    private boolean contieneLinks;
    private boolean contieneEmojis;

    public Mensaje(String fecha, String hora, String usuario, String contenido) {
        this.fecha = fecha;
        this.hora = hora;
        this.usuario = usuario;
        this.contenido = contenido;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getFechaNormalizada() { return fechaNormalizada; }
    public void setFechaNormalizada(String fechaNormalizada) { this.fechaNormalizada = fechaNormalizada; }

    public MessageType getTipoMensaje() { return tipoMensaje; }
    public void setTipoMensaje(MessageType tipoMensaje) { this.tipoMensaje = tipoMensaje; }

    public Integer getLongitudMensaje() { return longitudMensaje; }
    public void setLongitudMensaje(Integer longitudMensaje) { this.longitudMensaje = longitudMensaje; }

    public Integer getCantidadPalabras() { return cantidadPalabras; }
    public void setCantidadPalabras(Integer cantidadPalabras) { this.cantidadPalabras = cantidadPalabras; }

    public boolean isContieneLinks() { return contieneLinks; }
    public void setContieneLinks(boolean contieneLinks) { this.contieneLinks = contieneLinks; }

    public boolean isContieneEmojis() { return contieneEmojis; }
    public void setContieneEmojis(boolean contieneEmojis) { this.contieneEmojis = contieneEmojis; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensaje mensaje = (Mensaje) o;
        return contieneLinks == mensaje.contieneLinks &&
               contieneEmojis == mensaje.contieneEmojis &&
               Objects.equals(fecha, mensaje.fecha) &&
               Objects.equals(hora, mensaje.hora) &&
               Objects.equals(usuario, mensaje.usuario) &&
               Objects.equals(contenido, mensaje.contenido) &&
               Objects.equals(fechaNormalizada, mensaje.fechaNormalizada) &&
               tipoMensaje == mensaje.tipoMensaje &&
               Objects.equals(longitudMensaje, mensaje.longitudMensaje) &&
               Objects.equals(cantidadPalabras, mensaje.cantidadPalabras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, hora, usuario, contenido, fechaNormalizada, tipoMensaje, longitudMensaje, cantidadPalabras, contieneLinks, contieneEmojis);
    }

    @Override
    public String toString() {
        return "Mensaje{" +
               "fecha='" + fecha + '\'' +
               ", hora='" + hora + '\'' +
               ", usuario='" + usuario + '\'' +
               ", contenido='" + contenido + '\'' +
               ", fechaNormalizada='" + fechaNormalizada + '\'' +
               ", tipoMensaje=" + tipoMensaje +
               ", longitudMensaje=" + longitudMensaje +
               ", cantidadPalabras=" + cantidadPalabras +
               ", contieneLinks=" + contieneLinks +
               ", contieneEmojis=" + contieneEmojis +
               '}';
    }
}
