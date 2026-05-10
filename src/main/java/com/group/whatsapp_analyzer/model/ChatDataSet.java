package com.group.whatsapp_analyzer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
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

}
