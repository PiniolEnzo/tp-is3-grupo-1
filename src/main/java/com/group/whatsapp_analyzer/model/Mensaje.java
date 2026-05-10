package com.group.whatsapp_analyzer.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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

}
