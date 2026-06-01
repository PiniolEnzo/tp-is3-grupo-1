package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.dto.ActividadUsuarioDTO;
import com.group.whatsapp_analyzer.model.ChatDataSet;
import com.group.whatsapp_analyzer.model.Mensaje;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

@Service
public class EstadisticaService {

    private static final Set<String> STOPWORDS = Set.of(
            "de", "la", "el", "y", "que", "en", "a", "los", "las",
            "un", "una", "unos", "unas", "por", "para", "con",
            "no", "si", "se", "es", "al", "del", "lo", "le",
            "me", "te", "mi", "tu", "su", "yo", "vos", "él",
            "ella", "nosotros", "ustedes", "ellos", "ellas",
            "como", "pero", "más", "mas", "ya", "o", "u", "omitido", "multimedia", "mensaje"
    );

    // Actividad: Recuento de mensajes por usuario
    public ActividadUsuarioDTO contarMensajesPorUsuario(ChatDataSet chatDataSet) {

        List<Mensaje> mensajes = chatDataSet.getMensajes();
        Map<String, Integer> conteo = new HashMap<>();

        for (Mensaje m : mensajes) {
            String usuario = m.getUsuario();
            conteo.put(usuario, conteo.getOrDefault(usuario, 0) + 1);
        }

        String usuarioConMas = null;
        Integer maxMensajes = 0;

        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > maxMensajes) {
                maxMensajes = entry.getValue();
                usuarioConMas = entry.getKey();
            }
        }

        return ActividadUsuarioDTO.builder()
                .mensajesPorUsuario(conteo)
                .usuarioConMasMensajes(usuarioConMas)
                .cantidadMensajesUsuarioMasActivo(maxMensajes)
                .build();
    }

    // Actividad: Frecuencia de palabras
    public Map<String, Integer> contarFrecuencia(List<Mensaje> mensajes) {

        Map<String, Integer> frecuencia = new HashMap<>();

        for (Mensaje mensaje : mensajes) {

            String contenido = mensaje.getContenido()
                    .toLowerCase()
                    .replaceAll("[^a-zA-ZáéíóúñüÁÉÍÓÚÑ0-9 ]", "");

            String[] palabras = contenido.split("\\s+");

            for (String palabra : palabras) {
                if (!palabra.isBlank()
                        && !palabra.matches("\\d+")
                        && !STOPWORDS.contains(palabra)) {
                    frecuencia.put(
                            palabra,
                            frecuencia.getOrDefault(palabra, 0) + 1
                    );
                }
            }
        }

        return frecuencia.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // Actividad: Extracción y frecuencia de emojis
    public List<String> extraerEmojis(ChatDataSet chatDataSet) {
    List<String> emojis = new ArrayList<>();
    
    String baseRange = "(?:[\\x{1F600}-\\x{1F64F}]|[\\x{1F300}-\\x{1F5FF}]|[\\x{1F680}-\\x{1F6FF}]|" +
                       "[\\x{1F1E0}-\\x{1F1FF}]|[\\x{1F900}-\\x{1F9FF}]|[\\x{1FA70}-\\x{1FAFF}]|" +
                       "[\\x{2600}-\\x{26FF}]|[\\x{2700}-\\x{27BF}])";
    String flag = "(?:[\\x{1F1E0}-\\x{1F1FF}]{2})";
    String skinTone = "[\\x{1F3FB}-\\x{1F3FF}]";
    String zwj = "\\u200D";
    String vs16 = "\\uFE0F";
    
    String component = baseRange + skinTone + "?" + vs16 + "?";
    String fullEmoji = "(?:" + flag + "|" + component + "(?:" + zwj + component + ")*)";

    Pattern emojiPattern = Pattern.compile(fullEmoji, Pattern.UNICODE_CHARACTER_CLASS);

    for (Mensaje mensaje : chatDataSet.getMensajes()) {
        if (mensaje.isContieneEmojis()) {
            Matcher matcher = emojiPattern.matcher(mensaje.getContenido());
            while (matcher.find()) {
                emojis.add(matcher.group());
            }
        }
    }

    return emojis;
   }

    public Map<String, Integer> contarFrecuenciaEmojis(List<String> emojis) {
    Map<String, Integer> frecuencia = new HashMap<>();
    for (String emoji : emojis) {
        frecuencia.put(emoji, frecuencia.getOrDefault(emoji, 0) + 1);
    }
    return frecuencia.entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a, b) -> a,
                    LinkedHashMap::new
            ));
   }

    public String obtenerEmojiMasUtilizado(Map<String, Integer> frecuenciaEmojis) {
    String emojiMasUtilizado = null;
    int maxFrecuencia = 0;

    for (Map.Entry<String, Integer> entry : frecuenciaEmojis.entrySet()) {
        if (entry.getValue() > maxFrecuencia) {
            maxFrecuencia = entry.getValue();
            emojiMasUtilizado = entry.getKey();
        }
    }
    return emojiMasUtilizado;
   }

    // Actividad: Agrupar mensajes por día
    public Map<String, Integer> contarMensajesPorDia(List<Mensaje> mensajes) {
    Map<String, Integer> mensajesPorDia = new HashMap<>();
    for (Mensaje mensaje : mensajes) {
        String fechaCompleta = mensaje.getFechaNormalizada();
        String fecha = fechaCompleta.split(" ")[0];
        mensajesPorDia.put(fecha, mensajesPorDia.getOrDefault(fecha, 0) + 1);
    }
    return mensajesPorDia;
    }

    public Integer obtenerCantidadMaxima(Map<String, Integer> mensajesPorDia) {
    int maximo = 0;
    for (Integer cantidad : mensajesPorDia.values()) {
        if (cantidad > maximo) {
            maximo = cantidad;
        }
    }
    return maximo;
    }

    public List<String> obtenerDiasMasActivos(List<Mensaje> mensajes) {
    Map<String, Integer> mensajesPorDia = contarMensajesPorDia(mensajes);
    Integer maximo = obtenerCantidadMaxima(mensajesPorDia);
    List<String> diasMasActivos = new java.util.ArrayList<>();
    for (Map.Entry<String, Integer> entry : mensajesPorDia.entrySet()) {
        if (entry.getValue().equals(maximo)) {
            diasMasActivos.add(entry.getKey());
        }
    }
    return diasMasActivos;
    }


    // Actividad: Agrupar mensajes por hora
    public Map<Integer, Integer> obtenerFrecuenciaMensajesPorHora(ChatDataSet chatDataSet) {
        Map<Integer, Integer> frecuenciaPorHora = new HashMap<>();
        for (Mensaje mensaje : chatDataSet.getMensajes()) {
            int hora = obtenerHoraMensaje(mensaje);
            frecuenciaPorHora.put(hora, frecuenciaPorHora.getOrDefault(hora, 0) + 1);
        }
        return frecuenciaPorHora;
    }

    public int obtenerHoraMayorActividad(ChatDataSet chatDataSet) {
        Map<Integer, Integer> frecuenciaPorHora = obtenerFrecuenciaMensajesPorHora(chatDataSet);
        int horaMayorActividad = -1;
        int maxFrecuencia = -1;
        for (Map.Entry<Integer, Integer> entry : frecuenciaPorHora.entrySet()) {
            if (entry.getValue() > maxFrecuencia) {
                maxFrecuencia = entry.getValue();
                horaMayorActividad = entry.getKey();
            }
        }
        return horaMayorActividad;
    }

    private int obtenerHoraMensaje(Mensaje mensaje) {
        String horaMensaje = mensaje.getFechaNormalizada().substring(11, 13);
        return Integer.parseInt(horaMensaje);
    }

   
}