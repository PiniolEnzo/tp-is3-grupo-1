package com.group.whatsapp_analyzer.model;

import java.util.List;
import java.util.Objects;

public class ChatDataSet {
    private List<Mensaje> mensajes;
    private Integer totalMensajes;
    private Integer mensajesTexto;
    private Integer mensajesMultimedia;
    private Integer mensajesContacto;
    private Integer mensajesEnlace;
    private Integer mensajesEliminados;

    public ChatDataSet(List<Mensaje> mensajes){
        this.mensajes = mensajes;
        this.totalMensajes = mensajes.size();

        int texto = 0, multimedia = 0, contacto = 0, enlace = 0, eliminados = 0;
        for (Mensaje m : mensajes) {
            if (m.getTipoMensaje() == null) continue;
            switch (m.getTipoMensaje()) {
                case TEXTO -> texto++;
                case MULTIMEDIA -> multimedia++;
                case CONTACTO -> contacto++;
                case ENLACE -> enlace++;
                case ELIMINADO -> eliminados++;
            }
        }
        this.mensajesTexto = texto;
        this.mensajesMultimedia = multimedia;
        this.mensajesContacto = contacto;
        this.mensajesEnlace = enlace;
        this.mensajesEliminados = eliminados;
    }

    public List<Mensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }

    public Integer getTotalMensajes() { return totalMensajes; }
    public void setTotalMensajes(Integer totalMensajes) { this.totalMensajes = totalMensajes; }

    public Integer getMensajesTexto() { return mensajesTexto; }
    public void setMensajesTexto(Integer mensajesTexto) { this.mensajesTexto = mensajesTexto; }

    public Integer getMensajesMultimedia() { return mensajesMultimedia; }
    public void setMensajesMultimedia(Integer mensajesMultimedia) { this.mensajesMultimedia = mensajesMultimedia; }

    public Integer getMensajesContacto() { return mensajesContacto; }
    public void setMensajesContacto(Integer mensajesContacto) { this.mensajesContacto = mensajesContacto; }

    public Integer getMensajesEnlace() { return mensajesEnlace; }
    public void setMensajesEnlace(Integer mensajesEnlace) { this.mensajesEnlace = mensajesEnlace; }

    public Integer getMensajesEliminados() { return mensajesEliminados; }
    public void setMensajesEliminados(Integer mensajesEliminados) { this.mensajesEliminados = mensajesEliminados; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDataSet that = (ChatDataSet) o;
        return Objects.equals(mensajes, that.mensajes) &&
               Objects.equals(totalMensajes, that.totalMensajes) &&
               Objects.equals(mensajesTexto, that.mensajesTexto) &&
               Objects.equals(mensajesMultimedia, that.mensajesMultimedia) &&
               Objects.equals(mensajesContacto, that.mensajesContacto) &&
               Objects.equals(mensajesEnlace, that.mensajesEnlace) &&
               Objects.equals(mensajesEliminados, that.mensajesEliminados);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensajes, totalMensajes, mensajesTexto, mensajesMultimedia, mensajesContacto, mensajesEnlace, mensajesEliminados);
    }

    @Override
    public String toString() {
        return "ChatDataSet{" +
               "mensajes=" + mensajes +
               ", totalMensajes=" + totalMensajes +
               ", mensajesTexto=" + mensajesTexto +
               ", mensajesMultimedia=" + mensajesMultimedia +
               ", mensajesContacto=" + mensajesContacto +
               ", mensajesEnlace=" + mensajesEnlace +
               ", mensajesEliminados=" + mensajesEliminados +
               '}';
    }
}
