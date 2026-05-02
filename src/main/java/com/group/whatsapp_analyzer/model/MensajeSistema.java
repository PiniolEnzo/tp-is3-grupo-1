package com.group.whatsapp_analyzer.model;

public class MensajeSistema {
    private String fecha;
    private String hora;
    private String contenido;

    public MensajeSistema(String fecha, String hora, String contenido) {
        this.fecha = fecha;
        this.hora = hora;
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
