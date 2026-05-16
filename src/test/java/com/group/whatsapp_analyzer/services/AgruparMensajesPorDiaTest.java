package com.group.whatsapp_analyzer.services;
import com.group.whatsapp_analyzer.services.EstadisticaService;
import com.group.whatsapp_analyzer.services.ParserService;

import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import com.group.whatsapp_analyzer.model.ParseResult;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AgruparMensajesPorDiaTest {

    private final EstadisticaService estadisticaService =
        new EstadisticaService();
    private final ParserService parserService =
            new ParserService();

    @Test
    void testMensajesPorDiaDesdeArchivoTxt() throws Exception {

        System.out.println("\n=== TEST: Mensajes por día desde archivo TXT ===");

        String contenido =
                "31/03/26, 10:00 - Juan: Hola\n" +
                "31/03/26, 11:00 - Maria: Todo bien\n" +
                "01/04/26, 09:00 - Pedro: Buen día\n" +
                "01/04/26, 10:00 - Ana: Cómo están?\n" +
                "01/04/26, 11:00 - Juan: Bien";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "chat.txt",
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultadoParseo =
                parserService.leerArchivo(file);

        ChatDataSet chatDataSet =
                parserService.postProcesar(resultadoParseo);

        Map<String, Integer> resultado =
                estadisticaService.contarMensajesPorDia(
                        chatDataSet.getMensajes()
                );

        List<String> diasMasActivos =
                estadisticaService.obtenerDiasMasActivos(
                        chatDataSet.getMensajes()
                );

        System.out.println("\nMensajes por día:");
        System.out.println(resultado);

        System.out.println("\nDías más activos:");
        System.out.println(diasMasActivos);

        assertEquals(2, resultado.get("31/03/2026"));
        assertEquals(3, resultado.get("01/04/2026"));

        assertEquals(1, diasMasActivos.size());
        assertEquals("01/04/2026", diasMasActivos.get(0));
    }

    @Test
    void testContarMensajesPorDia() {

        List<Mensaje> mensajes = List.of(

                crearMensaje("31/03/2026 10:00"),
                crearMensaje("31/03/2026 11:00"),

                crearMensaje("01/04/2026 09:00"),
                crearMensaje("01/04/2026 10:00"),
                crearMensaje("01/04/2026 11:00")
        );

        Map<String, Integer> resultado =
                estadisticaService.contarMensajesPorDia(mensajes);

        assertEquals(2, resultado.get("31/03/2026"));
        assertEquals(3, resultado.get("01/04/2026"));
    }

    @Test
    void testObtenerDiasMasActivos() {

        List<Mensaje> mensajes = List.of(

                crearMensaje("31/03/2026 10:00"),

                crearMensaje("01/04/2026 09:00"),
                crearMensaje("01/04/2026 10:00"),

                crearMensaje("02/04/2026 11:00"),
                crearMensaje("02/04/2026 12:00")
        );

        List<String> resultado =
                estadisticaService.obtenerDiasMasActivos(mensajes);

        assertEquals(2, resultado.size());

        assertTrue(resultado.contains("01/04/2026"));
        assertTrue(resultado.contains("02/04/2026"));
    }

    private Mensaje crearMensaje(String fechaNormalizada) {

        Mensaje mensaje =
                new Mensaje(
                        "31/03/2026",
                        "10:00",
                        "Juan",
                        "Hola"
                );

        mensaje.setFechaNormalizada(fechaNormalizada);

        return mensaje;
    }
}