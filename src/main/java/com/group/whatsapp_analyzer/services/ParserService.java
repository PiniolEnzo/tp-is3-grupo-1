package com.group.whatsapp_analyzer.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    public List<String> leerArchivo(MultipartFile file) throws Exception {
         
    List<String> lineas = new ArrayList<>();
    
    try {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(file.getInputStream(), "UTF-8")
        );

        String linea;
        while ((linea = reader.readLine()) != null) {
            if (!linea.isBlank()) {
                lineas.add(linea);
            }
        }

        reader.close();
        
    } catch (Exception e) {
        throw new Exception("El archivo está corrupto o no se puede leer: " + e.getMessage());
    }
    
    return lineas;
    }
}