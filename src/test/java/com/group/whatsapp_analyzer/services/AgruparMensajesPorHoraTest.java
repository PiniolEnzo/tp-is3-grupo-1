package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AgruparMensajesPorHoraTest {

    private final EstadisticaService estadisticaService = new EstadisticaService();

    @Test
    void testObtenerFrecuenciaMensajesPorHora() {
        Mensaje m1 = new Mensaje("1/1/24", "10:00", "Juan", "Hola");
        m1.setFechaNormalizada("01/01/2024 10:30");

        Mensaje m2 = new Mensaje("1/1/24", "10:15", "Maria", "¿Cómo estás?");
        m2.setFechaNormalizada("01/01/2024 10:45");

        Mensaje m3 = new Mensaje("1/1/24", "14:00", "Pedro", "Buenas tardes");
        m3.setFechaNormalizada("01/01/2024 14:00");

        Mensaje m4 = new Mensaje("1/1/24", "14:30", "Ana", "Todo bien");
        m4.setFechaNormalizada("01/01/2024 14:30");

        Mensaje m5 = new Mensaje("1/1/24", "14:45", "Juan", "Genial");
        m5.setFechaNormalizada("01/01/2024 14:45");

        Mensaje m6 = new Mensaje("1/1/24", "20:00", "Luis", "Buenas noches");
        m6.setFechaNormalizada("01/01/2024 20:00");

        ChatDataSet dataset = new ChatDataSet(List.of(m1, m2, m3, m4, m5, m6));

        Map<Integer, Integer> frecuencia = estadisticaService.obtenerFrecuenciaMensajesPorHora(dataset);

        assertEquals(3, frecuencia.size());
        assertEquals(2, frecuencia.get(10));
        assertEquals(3, frecuencia.get(14));
        assertEquals(1, frecuencia.get(20));
    }

    @Test
    void testObtenerHoraMayorActividad() {
        Mensaje m1 = new Mensaje("1/1/24", "10:00", "Juan", "Hola");
        m1.setFechaNormalizada("01/01/2024 10:30");

        Mensaje m2 = new Mensaje("1/1/24", "10:15", "Maria", "¿Cómo estás?");
        m2.setFechaNormalizada("01/01/2024 10:45");

        Mensaje m3 = new Mensaje("1/1/24", "14:00", "Pedro", "Buenas tardes");
        m3.setFechaNormalizada("01/01/2024 14:00");

        Mensaje m4 = new Mensaje("1/1/24", "14:30", "Ana", "Todo bien");
        m4.setFechaNormalizada("01/01/2024 14:30");

        Mensaje m5 = new Mensaje("1/1/24", "14:45", "Juan", "Genial");
        m5.setFechaNormalizada("01/01/2024 14:45");

        Mensaje m6 = new Mensaje("1/1/24", "20:00", "Luis", "Buenas noches");
        m6.setFechaNormalizada("01/01/2024 20:00");

        ChatDataSet dataset = new ChatDataSet(List.of(m1, m2, m3, m4, m5, m6));

        int horaPico = estadisticaService.obtenerHoraMayorActividad(dataset);

        assertEquals(14, horaPico);
    }

    @Test
    void testObtenerHoraMayorActividadConEmpate() {
        Mensaje m1 = new Mensaje("1/1/24", "08:00", "Juan", "uno");
        m1.setFechaNormalizada("01/01/2024 08:00");
        Mensaje m2 = new Mensaje("1/1/24", "08:30", "Maria", "dos");
        m2.setFechaNormalizada("01/01/2024 08:30");
        Mensaje m3 = new Mensaje("1/1/24", "22:00", "Pedro", "tres");
        m3.setFechaNormalizada("01/01/2024 22:00");
        Mensaje m4 = new Mensaje("1/1/24", "22:30", "Ana", "cuatro");
        m4.setFechaNormalizada("01/01/2024 22:30");

        ChatDataSet dataset = new ChatDataSet(List.of(m1, m2, m3, m4));

        int horaPico = estadisticaService.obtenerHoraMayorActividad(dataset);

        assertEquals(2, estadisticaService.obtenerFrecuenciaMensajesPorHora(dataset).get(horaPico));
    }

    @Test
    void testObtenerFrecuenciaMensajesPorHoraConDatasetVacio() {
        ChatDataSet dataset = new ChatDataSet(List.of());

        Map<Integer, Integer> frecuencia = estadisticaService.obtenerFrecuenciaMensajesPorHora(dataset);

        assertEquals(0, frecuencia.size());
    }

    @Test
    void testObtenerHoraMayorActividadConDatasetVacio() {
        ChatDataSet dataset = new ChatDataSet(List.of());

        int horaPico = estadisticaService.obtenerHoraMayorActividad(dataset);

        assertEquals(-1, horaPico);
    }
}