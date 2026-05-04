package com.group.whatsapp_analyzer.model;

import java.util.ArrayList;
import java.util.List;

public class ParseResult {
    private int totalLineas = 0;
    private int lineasValidas = 0;
    private int lineasInvalidas = 0;
    private int mensajesSistema = 0;
    private String advertencia = null; // Para mostrar advertencias al usuario
    
    private List<Mensaje> mensajes = new ArrayList<>();
    private List<MensajeSistema> mensajesDelSistema = new ArrayList<>();

    public String getAdvertencia() {
        return advertencia;
    }

    public void setAdvertencia(String advertencia) {
        this.advertencia = advertencia;
    }

    public int getTotalLineas() {
        return totalLineas;
    }

    public void incrementarTotalLineas() {
        this.totalLineas++;
    }

    public int getLineasValidas() {
        return lineasValidas;
    }

    public void incrementarLineasValidas() {
        this.lineasValidas++;
    }

    public int getLineasInvalidas() {
        return lineasInvalidas;
    }

    public void incrementarLineasInvalidas() {
        this.lineasInvalidas++;
    }

    public int getMensajesSistema() {
        return mensajesSistema;
    }

    public void incrementarMensajesSistema() {
        this.mensajesSistema++;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void agregarMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public List<MensajeSistema> getMensajesDelSistema() {
        return mensajesDelSistema;
    }

    public void agregarMensajeSistema(MensajeSistema mensajeSistema) {
        this.mensajesDelSistema.add(mensajeSistema);
        this.incrementarMensajesSistema();
    }
}
