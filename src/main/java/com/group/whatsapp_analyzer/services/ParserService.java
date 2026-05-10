package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.exceptions.*;
import com.group.whatsapp_analyzer.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserService {

    // Patrón regex para identificar líneas válidas de WhatsApp
    // Soporta formatos de fecha en español e inglés, ej: 31/12/23, 23:59 o 12/31/23, 11:59 PM
    private static final Pattern PATTERN = Pattern.compile("^\\[?(\\d{1,2}[\\/\\-]\\d{1,2}[\\/\\-]\\d{2,4})[\\h,]+(\\d{1,2}:\\d{2}(?::\\d{2})?(?:\\h?[aApP]\\.?\\h?[mM]\\.?)?)\\]?[\\h\\-]+(.*)$");

    public ParseResult leerArchivo(MultipartFile file) {
         
        ParseResult resultado = new ParseResult();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            Mensaje ultimoMensaje = null;

            while ((linea = reader.readLine()) != null) {
                resultado.incrementarTotalLineas();

                try {
                    if (esLineaValida(linea)) {
                        String[] partes = parsearLinea(linea);

                        if (partes == null || partes.length < 4) {
                            throw new LineFormatException("La línea no tiene el formato esperado: " + linea);
                        }

                        String fecha = partes[0];
                        String hora = partes[1];
                        String usuario = partes[2];
                        String contenido = partes[3];

                        if (usuario != null) {
                            // Mensaje normal de usuario
                            ultimoMensaje = new Mensaje(fecha, hora, usuario, contenido);
                            resultado.agregarMensaje(ultimoMensaje);
                            resultado.incrementarLineasValidas();
                        } else {
                            // Mensaje de sistema
                            resultado.agregarMensajeSistema(new MensajeSistema(fecha, hora, contenido));
                            ultimoMensaje = null; // Reiniciar porque los msjs de sistema rara vez son multilinea
                        }
                    } else {
                        // Línea de continuación de mensaje o inválida
                        if (ultimoMensaje != null) {
                            // Concatenamos la línea al último mensaje válido
                            ultimoMensaje.setContenido(ultimoMensaje.getContenido() + "\n" + linea);
                        } else {
                            // Si no hay un mensaje previo válido, y no es vacía, es inválida
                            if (!linea.isBlank()) {
                                resultado.incrementarLineasInvalidas();
                            }
                        }
                    }
                } catch (DateTimeParseException e) {
                    throw new DateInvalidException("Fecha inválida: "+ linea, e);
                } catch (IllegalArgumentException e){
                    throw new InvalidMessageException("Mensaje inválido: " + linea, e);
                }
            }
            
        } catch (IOException e) {
            throw new InvalidFileException("Error al leer el archivo: " + e.getMessage(), e);
        }

        // Checklist: Si hay más del 10% de líneas inválidas, mostrar advertencia al usuario
        if (resultado.getTotalLineas() > 0) {
            double porcentajeInvalidas = (double) resultado.getLineasInvalidas() / resultado.getTotalLineas();
            if (porcentajeInvalidas > 0.10) {
                // Formateamos el porcentaje para mostrarlo (ej: 15.5%)
                String porcentajeStr = String.format(java.util.Locale.US, "%.1f", porcentajeInvalidas * 100);
                resultado.setAdvertencia("Atención: El " + porcentajeStr + "% de las líneas son inválidas. Es posible que el archivo no sea un chat de WhatsApp o esté dañado.");
            }
        }
        
        return resultado;
    }

    private boolean esLineaValida(String linea) {
        return PATTERN.matcher(linea).matches();
    }

    private String[] parsearLinea(String linea) {
        Matcher m = PATTERN.matcher(linea);
        if (m.matches()) {
            String fecha = m.group(1);
            String hora = m.group(2);
            String resto = m.group(3);

            int colonIndex = resto.indexOf(": ");
            if (colonIndex != -1) {
                String usuario = resto.substring(0, colonIndex);
                String contenido = resto.substring(colonIndex + 2);
                return new String[]{fecha, hora, usuario, contenido};
            } else {
                // Es un mensaje del sistema, devolvemos null en el espacio del usuario
                return new String[]{fecha, hora, null, resto};
            }
        }
        return null;
    }

public ChatDataSet postProcesar(ParseResult resultado) {
        List<Mensaje> mensajesProcesados = new ArrayList<>();

        for (Mensaje m : resultado.getMensajes()) {
            m.setUsuario(normalizarUsuario(m.getUsuario()));
            m.setContenido(limpiarContenido(m.getContenido()));
            if (!esMensajeValido(m.getContenido())) {
                continue;
            }
            m.setFechaNormalizada(normalizarFecha(m.getFecha(), m.getHora()));
            m.setTipoMensaje(clasificarMensaje(m.getContenido()));
            agregarMetadata(m);
            mensajesProcesados.add(m);
        }

        return new ChatDataSet(mensajesProcesados);
}

    private String normalizarUsuario(String usuario) {
        if (usuario == null) return null;
        return usuario.strip().replaceAll("\\s+", " ");
    }

    private String limpiarContenido(String contenido) {
        if (contenido == null) return null;
        return contenido.replace("\t", " ")
                .replace("\u200E", "") // Eliminar caracteres de control invisibles
                .replace("\u200B", "") // Eliminar caracteres de espacio cero ancho
                .replace("\u200C", "")
                .replace("\u200D", "")
                .replace("\uFEFF", "")
                .replace("\u202f", " ")
                .replace("\u00a0", " ")
                .replaceAll("[\\u2066-\\u2069]", "") // Eliminar caracteres de control de formato de texto (LRI, RLI, FSI, PDI)
                .replaceAll("\\s+", " ") // Reemplazar múltiples espacios por uno solo
                .strip();
    }

    private String normalizarFecha(String fecha, String hora) {
        try {
            fecha = fecha
                    .replaceAll("\\p{Cf}", "") // eliminar caracteres de formato invisibles
                    .replaceAll("\\u202f", " ")   // narrow no-break space
                    .replaceAll("\\u00a0", " ")   // no-break space
                    .replaceAll("\\u2007", " ")   // figure space
                    .replaceAll("\\u2009", " ")   // thin space
                    // normaliza múltiples espacios
                    .replaceAll("\\s+", " ")
                    .strip();

            hora = hora
                    .replace(".", "")
                    .replaceAll("\\p{Cf}", "") // eliminar caracteres de formato invisibles
                    .replaceAll("\\u202f", " ")   // narrow no-break space
                    .replaceAll("\\u00a0", " ")   // no-break space
                    .replaceAll("\\u2007", " ")   // figure space
                    .replaceAll("\\u2009", " ")   // thin space
                    // normaliza múltiples espacios
                    .replaceAll("\\s+", " ")
                    // normaliza AM/PM con espacios raros
                    .replaceAll("A\\s*M", "AM")
                    .replaceAll("P\\s*M", "PM")
                    .replaceAll("a\\s*m", "AM")
                    .replaceAll("p\\s*m", "PM")
                    .strip();

            // parsear la hora
            boolean tieneAMPM = hora.contains("AM") || hora.contains("PM");

            List<DateTimeFormatter> formatosHora = tieneAMPM
                    ? List.of(
                    DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
                    DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH)
            )
                    : List.of(
                    DateTimeFormatter.ofPattern("H:mm"),
                    DateTimeFormatter.ofPattern("H:mm:ss")
            );

            LocalTime time = null;

            for (DateTimeFormatter f : formatosHora) {
                try {
                    time = LocalTime.parse(hora, f);
                    break;
                } catch (Exception ignored) {}
            }

            if (time == null) {
                throw new TimeInvalidException("Hora inválida: " + hora);
            }

            // parsear la fecha
            List<DateTimeFormatter> formatos = List.of(
                    DateTimeFormatter.ofPattern("d/M/yy"),
                    DateTimeFormatter.ofPattern("d-M-yy"),
                    DateTimeFormatter.ofPattern("d/M/yyyy"),
                    DateTimeFormatter.ofPattern("d-M-yyyy"),
                    DateTimeFormatter.ofPattern("M/d/yy"),
                    DateTimeFormatter.ofPattern("M-d-yy"),
                    DateTimeFormatter.ofPattern("M/d/yyyy"),
                    DateTimeFormatter.ofPattern("M-d-yyyy")
            );

            LocalDate date = null;

            for (DateTimeFormatter dtf : formatos) {
                try {
                    date = LocalDate.parse(fecha, dtf);
                    break;
                } catch (Exception ignored) {}
            }

            if (date == null) {
                throw new DateInvalidException("Fecha inválida: " + fecha);
            }

            // unificar fecha y hora en un LocalDateTime

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            // pasar el LocalDateTime al formato final dd/MM/yyyy HH:mm

            return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        } catch (Exception e) {
            throw new DateInvalidException(
                    "Error normalizando fecha/hora: " + fecha + " " + hora, e
            );
        }
    }

    private MessageType clasificarMensaje(String contenido) {
        if (contenido == null || contenido.isBlank()) {
            return null;
        }

        if (contenido.contains("<Multimedia omitido>")) {
            return MessageType.MULTIMEDIA;
        }

        if(contenido.matches(".*\\.vcf \\(archivo adjunto\\)")){
            return MessageType.CONTACTO;
        }

        if(contenido.matches("^(https?://)?([\\w-]+\\.)+[\\w-]+(/\\S*)?$")){
            return MessageType.ENLACE;
        }

        if(contenido.contains("Se eliminó este mensaje.") || contenido.contains("Eliminaste este mensaje.")){
            return MessageType.ELIMINADO;
        }

        return MessageType.TEXTO;
    }

    private boolean esMensajeValido(String contenido) {
        return contenido != null && !contenido.isBlank();
    }

    private void agregarMetadata(Mensaje mensaje) {

        String contenido = mensaje.getContenido();

        mensaje.setLongitudMensaje(contenido.length());

        mensaje.setCantidadPalabras(contenido.split("\\s+").length);

        mensaje.setContieneLinks(contenido.matches("(?i).*((https?://|www\\.)\\S+|([a-z0-9-]+\\.)+[a-z]{2,}(\\/\\S*)?).*"));

        mensaje.setContieneEmojis(contenido.matches("(?s).*(?:[\\x{1F600}-\\x{1F64F}]|[\\x{1F300}-\\x{1F5FF}]|[\\x{1F680}-\\x{1F6FF}]|[\\x{1F1E0}-\\x{1F1FF}]|[\\x{1F900}-\\x{1F9FF}]|[\\x{1FA70}-\\x{1FAFF}]|[\\x{2600}-\\x{26FF}]|[\\x{2700}-\\x{27BF}]|[\\x{FE0F}]|[\\x{200D}]).*"));

    }

}