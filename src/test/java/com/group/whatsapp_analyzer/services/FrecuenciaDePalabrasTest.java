package com.group.whatsapp_analyzer.services;
import com.group.whatsapp_analyzer.services.EstadisticaService;

import com.group.whatsapp_analyzer.model.Mensaje;
import com.group.whatsapp_analyzer.model.ParseResult;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FrecuenciaDePalabrasTest {

    private final EstadisticaService estadisticaService = new EstadisticaService();
    private final ParserService parserService = new ParserService();

    @Test
    void testFrecuenciaDePalabrasDesdeArchivoTxt() throws Exception {

        String contenido = "31/12/23, 23:59 - Juan: Hola mundo\n" +
                "31/12/23, 23:59 - Maria: Hola Juan\n" +
                "01/01/24, 00:01 - Pedro: Mundo hola!";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "chat.txt",
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultadoParseo = parserService.leerArchivo(file);

        Map<String, Integer> frecuencia =
                estadisticaService.contarFrecuencia(resultadoParseo.getMensajes());

        assertEquals(3, frecuencia.get("hola"));
        assertEquals(2, frecuencia.get("mundo"));
        assertEquals(1, frecuencia.get("juan"));

        System.out.println(frecuencia);
    }

    @Test
    void testContarFrecuencia() {

        List<Mensaje> mensajes = List.of(

                new Mensaje(
                        "01/01/24",
                        "10:00",
                        "Juan",
                        "Hola mundo"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:01",
                        "Ana",
                        "Hola Juan"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:02",
                        "Pedro",
                        "Mundo hola!"
                )
        );

        Map<String, Integer> resultado =
                estadisticaService.contarFrecuencia(mensajes);

        assertEquals(3, resultado.get("hola"));
        assertEquals(2, resultado.get("mundo"));
        assertEquals(1, resultado.get("juan"));

        System.out.println(resultado);
    }

    @Test
    void testFrecuenciaDePalabrasConNormalizacion() {

        List<Mensaje> mensajes = List.of(

                new Mensaje(
                        "01/01/24",
                        "10:00",
                        "Juan",
                        "Hola!"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:01",
                        "Ana",
                        "HOLA"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:02",
                        "Pedro",
                        "hola 😊"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:03",
                        "Maria",
                        "Mundo, mundo."
                )
        );

        Map<String, Integer> resultado =
                estadisticaService.contarFrecuencia(mensajes);

        // Verificamos normalización
        assertEquals(3, resultado.get("hola"));

        // Verificamos eliminación de signos
        assertEquals(2, resultado.get("mundo"));

        // Verificamos que existan las palabras
        assertTrue(resultado.containsKey("hola"));
        assertTrue(resultado.containsKey("mundo"));

        System.out.println(resultado);
    }

    @Test
    void testStopwordsYNumerosNoSeCuentan() {

        List<Mensaje> mensajes = List.of(

                new Mensaje(
                        "01/01/24",
                        "10:00",
                        "Juan",
                        "Hola y hola de la casa"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:01",
                        "Ana",
                        "El mundo es hola 2024"
                ),

                new Mensaje(
                        "01/01/24",
                        "10:02",
                        "Pedro",
                        "Hola mundo 123"
                )
        );

        Map<String, Integer> resultado =
                estadisticaService.contarFrecuencia(mensajes);

        // Verificamos palabras válidas
        assertEquals(4, resultado.get("hola"));
        assertEquals(2, resultado.get("mundo"));
        assertEquals(1, resultado.get("casa"));

        // Verificamos que las stopwords NO existan
        assertFalse(resultado.containsKey("y"));
        assertFalse(resultado.containsKey("de"));
        assertFalse(resultado.containsKey("la"));
        assertFalse(resultado.containsKey("el"));
        assertFalse(resultado.containsKey("es"));

        // Verificamos que números NO existan
        assertFalse(resultado.containsKey("2024"));
        assertFalse(resultado.containsKey("123"));

        System.out.println(resultado);
    }
}