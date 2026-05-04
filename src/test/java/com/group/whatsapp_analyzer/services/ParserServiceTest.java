package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.model.ParseResult;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ParserServiceTest {

    private final ParserService parserService = new ParserService();

    @Test
    void testEsLineaValida() {
        assertTrue(parserService.esLineaValida("31/12/23, 23:59 - Juan: Hola"));
        assertTrue(parserService.esLineaValida("12/31/2023, 11:59 PM - Maria: Hello"));
        assertTrue(parserService.esLineaValida("1/1/24, 01:20 - Los mensajes y las llamadas están cifrados de extremo a extremo"));
        
        assertFalse(parserService.esLineaValida("Hola, ¿cómo estás?"));
        assertFalse(parserService.esLineaValida("31/12/23 - Falta la hora"));
    }

    @Test
    void testParsearLineaNormal() {
        String[] partes = parserService.parsearLinea("31/12/23, 23:59 - Juan: Hola");
        assertNotNull(partes);
        assertEquals("31/12/23", partes[0]);
        assertEquals("23:59", partes[1]);
        assertEquals("Juan", partes[2]);
        assertEquals("Hola", partes[3]);
    }

    @Test
    void testParsearLineaSistema() {
        String[] partes = parserService.parsearLinea("31/12/23, 23:59 - Fulano eliminó este mensaje");
        assertNotNull(partes);
        assertEquals("31/12/23", partes[0]);
        assertEquals("23:59", partes[1]);
        assertNull(partes[2]); // El usuario null indica que es un mensaje del sistema
        assertEquals("Fulano eliminó este mensaje", partes[3]);
    }

    @Test
    void testLeerArchivoConMultiplesCasos() throws Exception {
        String contenido = "Línea basura inicial\n" +
                "31/12/23, 23:59 - Juan: Hola\n" +
                "31/12/23, 23:59 - Maria: Hola Juan\n" +
                "Como andas?\n" + // Mensaje multilínea (continuación del de Maria)
                "1/1/24, 01:20 - Mensaje de sistema puro\n" +
                "Línea inválida suelta que debería aumentar el contador de inválidas";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "chat.txt",
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        // Verificamos las estadísticas
        assertEquals(6, resultado.getTotalLineas());
        assertEquals(2, resultado.getLineasValidas()); // Juan y Maria
        assertEquals(2, resultado.getLineasInvalidas()); // Línea basura inicial y la última línea huérfana
        assertEquals(1, resultado.getMensajesSistema()); // El mensaje puro del sistema
        
        // Verificamos el mensaje multilínea de Maria
        assertEquals("Hola Juan\nComo andas?", resultado.getMensajes().get(1).getContenido());
        
        // Verificamos el mensaje del sistema
        assertEquals("Mensaje de sistema puro", resultado.getMensajesDelSistema().get(0).getContenido());

        // Verificamos que se haya generado la advertencia por superar el 10% de inválidas
        assertNotNull(resultado.getAdvertencia());
        assertTrue(resultado.getAdvertencia().contains("Atención"));
    }
}
