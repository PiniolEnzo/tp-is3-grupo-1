package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.ParseResult;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrecuenciaDeEmojiServiceTest {

    private final EstadisticaService estadisticaService = new EstadisticaService();
    private final ParserService parserService = new ParserService();

    @Test
    void testEmojisDesdeArchivoTxt() throws Exception {
        System.out.println("\n=== TEST: Emojis desde archivo TXT ===");

        String contenido =
                "01/01/24, 12:00 - Juan: Hola 😂\n" +
                "01/01/24, 12:01 - Maria: Todo bien? 😂👍\n" +
                "01/01/24, 12:02 - Juan: Si, todo bien 👍👍\n" +
                "01/01/24, 12:03 - Maria: Que bueno 🚀";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "chat_emojis.txt",
                "text/plain",
                contenido.getBytes(StandardCharsets.UTF_8)
        );

        ParseResult resultadoParseo = parserService.leerArchivo(file);
        ChatDataSet chatDataSet = parserService.postProcesar(resultadoParseo);

        List<String> emojisExtraidos = estadisticaService.extraerEmojis(chatDataSet);
        Map<String, Integer> frecuencia = estadisticaService.contarFrecuenciaEmojis(emojisExtraidos);
        String emojiMasUtilizado = estadisticaService.obtenerEmojiMasUtilizado(frecuencia);

        System.out.println("\nEmojis extraídos: " + emojisExtraidos);
        System.out.println("\nFrecuencia de emojis: " + frecuencia);
        System.out.println("\nEmoji más utilizado: " + emojiMasUtilizado);

        assertEquals(6, emojisExtraidos.size());
        assertEquals(2, frecuencia.get("😂"));
        assertEquals(3, frecuencia.get("👍"));
        assertEquals(1, frecuencia.get("🚀"));
        assertEquals("👍", emojiMasUtilizado);
    }

    @Test
    void testContarFrecuenciaEmojis() {
        List<String> emojis = List.of("😂", "👍", "👍", "🚀", "😂", "👍");
        Map<String, Integer> resultado = estadisticaService.contarFrecuenciaEmojis(emojis);

        assertEquals(2, resultado.get("😂"));
        assertEquals(3, resultado.get("👍"));
        assertEquals(1, resultado.get("🚀"));
    }

    @Test
    void testObtenerEmojiMasUtilizado() {
        Map<String, Integer> frecuencia = Map.of(
                "😂", 2,
                "👍", 5,
                "🚀", 1
        );

        String resultado = estadisticaService.obtenerEmojiMasUtilizado(frecuencia);
        assertEquals("👍", resultado);
    }
}