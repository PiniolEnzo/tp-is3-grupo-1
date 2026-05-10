package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.exceptions.DateInvalidException;
import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import com.group.whatsapp_analyzer.model.MessageType;
import com.group.whatsapp_analyzer.model.ParseResult;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ParserServiceTest {

    private final ParserService parserService = new ParserService();

    // Tests de parseo a través de leerArchivo

    @Test
    void testLineaValidaEInvalida() throws Exception {
        // Las líneas inválidas deben ir ANTES de la primera línea válida.
        // Si van después, se tratan como continuación multilínea (comportamiento esperado).
        String contenido = "Hola, ¿cómo estás?\n" +                                // inválida (sin fecha/hora)
                "31/12/23 - Falta la hora\n" +                                     // inválida (formato incompleto)
                "31/12/23, 23:59 - Juan: Hola";                                    // válida

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getLineasValidas());
        assertEquals(2, resultado.getLineasInvalidas());
    }

    @Test
    void testLineaSistema() throws Exception {
        String contenido = "31/12/23, 23:59 - Los mensajes y las llamadas están cifrados de extremo a extremo";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getMensajesSistema());
        assertEquals("Los mensajes y las llamadas están cifrados de extremo a extremo",
                resultado.getMensajesDelSistema().get(0).getContenido());
    }

    @Test
    void testParseoFormatoNormal() throws Exception {
        String contenido = "31/12/23, 23:59 - Juan: Hola";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getLineasValidas());
        assertEquals("31/12/23", resultado.getMensajes().get(0).getFecha());
        assertEquals("23:59", resultado.getMensajes().get(0).getHora());
        assertEquals("Juan", resultado.getMensajes().get(0).getUsuario());
        assertEquals("Hola", resultado.getMensajes().get(0).getContenido());
    }

    @Test
    void testParseoFormatoAmPmEspaniol() throws Exception {
        String contenido = "9/5/2019, 7:49 a. m. - Nacho: Hola hermosa";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getLineasValidas());
        assertEquals("9/5/2019", resultado.getMensajes().get(0).getFecha());
        assertTrue(resultado.getMensajes().get(0).getHora().contains("a"));
        assertTrue(resultado.getMensajes().get(0).getHora().contains("m"));
        assertEquals("Nacho", resultado.getMensajes().get(0).getUsuario());
        assertEquals("Hola hermosa", resultado.getMensajes().get(0).getContenido());
    }

    @Test
    void testParseoFormatoAmPmNarrowSpace() throws Exception {
        // U+202F = NARROW NO-BREAK SPACE (usado por WhatsApp en español)
        String narrowSpace = "\u202F";
        String contenido = "9/5/2019, 7:49" + narrowSpace + "a." + narrowSpace + "m. - Nacho: Hola hermosa";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getLineasValidas());
        assertEquals("9/5/2019", resultado.getMensajes().get(0).getFecha());
        assertTrue(resultado.getMensajes().get(0).getHora().contains(narrowSpace));
        assertEquals("Nacho", resultado.getMensajes().get(0).getUsuario());
        assertEquals("Hola hermosa", resultado.getMensajes().get(0).getContenido());
    }

    @Test
    void testParseoMensajeSistemaFormatoAmPm() throws Exception {
        String contenido = "9/5/2019, 7:49 a. m. - Los mensajes y las llamadas están cifrados de extremo a extremo";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        assertEquals(1, resultado.getMensajesSistema());
        assertEquals("Los mensajes y las llamadas están cifrados de extremo a extremo",
                resultado.getMensajesDelSistema().get(0).getContenido());
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

    // Tests de PostProcesamiento

    @Test
    void testPostProcesarUsuariosConFormatosInconsistentes() throws Exception {
        // Usuarios con espacios extra, que deben normalizarse
        String contenido = "31/12/23, 23:59 -   Juan   Pérez  : Hola\n" +       // espacios extra
                "1/1/24, 10:00 -   Maria   : ¿Cómo estás?";                      // espacios antes/después

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        assertEquals("Juan Pérez", dataset.getMensajes().get(0).getUsuario());
        assertEquals("Maria", dataset.getMensajes().get(1).getUsuario());
    }

    @Test
    void testPostProcesarMensajesVacios() throws Exception {
        // Mensajes que después de limpiar quedan vacíos → deben filtrarse
        String contenido = "31/12/23, 23:59 - Juan: Hola\n" +
                "1/1/24, 10:00 - Maria:      \n" +                // solo espacios
                "2/1/24, 11:00 - Pedro: \t \n" +                   // tabs y espacios
                "3/1/24, 12:00 - Ana: Mensaje válido";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        // Maria y Pedro deberían ser filtrados por contenido vacío después de limpiar
        assertEquals(2, dataset.getTotalMensajes());
        assertEquals("Hola", dataset.getMensajes().get(0).getContenido());
        assertEquals("Mensaje válido", dataset.getMensajes().get(1).getContenido());
    }

    @Test
    void testPostProcesarCaracteresEspeciales() throws Exception {
        // Mensajes con caracteres de control, tabs, unicode invisible
        String contenido = "31/12/23, 23:59 - Juan: Hola\tMundo\n" +                           // tab
                "1/1/24, 10:00 - Maria: Texto \u200Einvisible\u200B!\n" +                       // LRM + ZWSP
                "2/1/24, 11:00 - Pedro: Múltiples   espacios   acá";                            // espacios duplicados

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        assertEquals("Hola Mundo", dataset.getMensajes().get(0).getContenido());
        // \u200E (LRM) y \u200B (ZWSP) se eliminan sin dejar espacio → "Texto invisible!"
        assertEquals("Texto invisible!", dataset.getMensajes().get(1).getContenido());
        assertEquals("Múltiples espacios acá", dataset.getMensajes().get(2).getContenido());
    }

    @Test
    void testPostProcesarClasificacionMensajes() throws Exception {
        String contenido = "31/12/23, 23:59 - Juan: Hola\n" +                                    // TEXTO
                "1/1/24, 10:00 - Maria: <Multimedia omitido>\n" +                                // MULTIMEDIA
                "2/1/24, 11:00 - Pedro: https://google.com\n" +                                  // ENLACE
                "3/1/24, 12:00 - Ana: contacto.vcf (archivo adjunto)\n" +                        // CONTACTO
                "4/1/24, 13:00 - Luis: Se eliminó este mensaje.\n" +                             // ELIMINADO
                "5/1/24, 14:00 - Juan: www.example.com/path";                                    // ENLACE

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        assertEquals(MessageType.TEXTO, dataset.getMensajes().get(0).getTipoMensaje());
        assertEquals(MessageType.MULTIMEDIA, dataset.getMensajes().get(1).getTipoMensaje());
        assertEquals(MessageType.ENLACE, dataset.getMensajes().get(2).getTipoMensaje());
        assertEquals(MessageType.CONTACTO, dataset.getMensajes().get(3).getTipoMensaje());
        assertEquals(MessageType.ELIMINADO, dataset.getMensajes().get(4).getTipoMensaje());
        assertEquals(MessageType.ENLACE, dataset.getMensajes().get(5).getTipoMensaje());

        // Verificar contadores en ChatDataSet
        assertEquals(6, dataset.getTotalMensajes());
        assertEquals(1, dataset.getMensajesTexto());
        assertEquals(1, dataset.getMensajesMultimedia());
        assertEquals(1, dataset.getMensajesContacto());
        assertEquals(2, dataset.getMensajesEnlace());
        assertEquals(1, dataset.getMensajesEliminados());
    }

    @Test
    void testPostProcesarContieneLinksYEmojis() throws Exception {
        String contenido = "31/12/23, 23:59 - Juan: Visita https://example.com ahora\n" +   // contiene link
                "1/1/24, 10:00 - Maria: Sin link\n" +                                        // sin link
                "2/1/24, 11:00 - Pedro: Mensaje con emoji 😀\n" +                            // contiene emoji
                "3/1/24, 12:00 - Ana: Normal";                                              // sin emoji

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        // Links
        assertTrue(dataset.getMensajes().get(0).isContieneLinks());
        assertFalse(dataset.getMensajes().get(1).isContieneLinks());

        // Emojis
        assertTrue(dataset.getMensajes().get(2).isContieneEmojis());
        assertFalse(dataset.getMensajes().get(3).isContieneEmojis());
    }

    @Test
    void testPostProcesarMetadata() throws Exception {
        String contenido = "31/12/23, 23:59 - Juan: Hola mundo";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);

        Mensaje msg = dataset.getMensajes().get(0);

        assertEquals(Integer.valueOf(10), msg.getLongitudMensaje());       // "Hola mundo" = 10 chars
        assertEquals(Integer.valueOf(2), msg.getCantidadPalabras());       // "Hola" y "mundo" = 2 palabras
    }

    @Test
    void testPostProcesarFechaInvalida() throws Exception {
        // Fecha numéricamente inválida que pasa el regex pero no el parseo
        String contenido = "32/01/24, 23:59 - Juan: Este mensaje tiene fecha inválida";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        // leerArchivo() funciona (el regex no valida semántica de fechas)
        ParseResult resultado = parserService.leerArchivo(file);

        // postProcesar() debe lanzar DateInvalidException al normalizar la fecha
        assertThrows(DateInvalidException.class, () -> parserService.postProcesar(resultado));
    }

    @Test
    void testPostProcesarFechaIncompleta() throws Exception {
        // Hora incompleta (solo HH sin MM) — no debería matchear el regex
        String contenido = "31/12/23, 23 - Juan: Esta línea no es válida como chat de WhatsApp";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);

        // No debería parsear como línea válida por formato de hora incompleto
        assertEquals(0, resultado.getLineasValidas());
        assertEquals(1, resultado.getLineasInvalidas());
    }

    @Test
    void testUltimo() throws Exception{
        String contenido = "9/5/2019, 7:49 a. m. - Los mensajes y las llamadas están cifrados de extremo a extremo. Solo las personas en este chat pueden leerlos, escucharlos o compartirlos. *Más información*\n" +
                "9/5/2019, 7:49 a. m. - Nacho: Hola hermosha\n" +
                "9/5/2019, 7:50 a. m. - Nacho: La dirección de Marcos es Alvear 965";

        MockMultipartFile file = new MockMultipartFile(
                "file", "chat.txt", "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultado = parserService.leerArchivo(file);
        ChatDataSet dataset = parserService.postProcesar(resultado);
        assertEquals(2, dataset.getMensajes().size());
        assertEquals("Hola hermosha", dataset.getMensajes().get(0).getContenido());
        assertEquals("La dirección de Marcos es Alvear 965", dataset.getMensajes().get(1).getContenido());
        assertEquals(MessageType.TEXTO, dataset.getMensajes().get(0).getTipoMensaje());
        assertEquals(MessageType.TEXTO, dataset.getMensajes().get(1).getTipoMensaje());

    }

}
