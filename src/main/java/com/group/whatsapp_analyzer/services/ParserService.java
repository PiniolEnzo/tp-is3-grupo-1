package com.group.whatsapp_analyzer.services;

import com.group.whatsapp_analyzer.model.Mensaje;
import com.group.whatsapp_analyzer.model.ParseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserService {

    // Patrón regex para identificar líneas válidas de WhatsApp
    // Soporta formatos de fecha en español e inglés, ej: 31/12/23, 23:59 o 12/31/23, 11:59 PM
    private static final Pattern PATTERN = Pattern.compile("^\\[?(\\d{1,2}[\\/\\-]\\d{1,2}[\\/\\-]\\d{2,4})[ ,]+(\\d{1,2}:\\d{2}(?::\\d{2})?(?:[ ]?[aApP]\\.?[mM]\\.?)?)\\]?[ \\-]+(.*)$");

    public ParseResult leerArchivo(MultipartFile file) throws Exception {
         
        ParseResult resultado = new ParseResult();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {

            String linea;
            Mensaje ultimoMensaje = null;

            while ((linea = reader.readLine()) != null) {
                resultado.incrementarTotalLineas();

                if (esLineaValida(linea)) {
                    String[] partes = parsearLinea(linea);
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
                        resultado.agregarMensajeSistema(new com.group.whatsapp_analyzer.model.MensajeSistema(fecha, hora, contenido));
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
            }
            
        } catch (Exception e) {
            throw new Exception("El archivo está corrupto o no se puede leer: " + e.getMessage());
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

    public boolean esLineaValida(String linea) {
        return PATTERN.matcher(linea).matches();
    }

    public String[] parsearLinea(String linea) {
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
}