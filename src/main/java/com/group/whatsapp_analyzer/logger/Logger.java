package com.group.whatsapp_analyzer.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private final ObjectMapper objectMapper;
    private final String logsDir;
    private final String docFile;

    private Logger() {
        this.logsDir = System.getProperty("app.logs.dir", "logs");
        this.docFile = this.logsDir + "/logs_info.txt";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ensureDocFileExists();
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String level, String message, String context) {
        try {
            ZonedDateTime now = ZonedDateTime.now();
            LogEntry entry = new LogEntry(
                now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                level,
                message,
                context
            );

            String fileName = String.format("%s/logs_%s.jsonl", 
                logsDir, 
                now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HH")));
            
            File file = new File(fileName);
            
            // Append JSON entry as a line (JSONL format is usually better for logs)
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
                out.println(objectMapper.writeValueAsString(entry));
            }
        } catch (Exception e) {
            System.err.println("Logging failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ensureDocFileExists() {
        File file = new File(docFile);
        if (!file.exists()) {
            try {
                Files.createDirectories(Paths.get(logsDir));
                String content = "Log System Documentation\n" +
                                  "=========================\n" +
                                  "Format: JSON Lines (JSONL)\n" +
                                  "Schema: {timestamp, level, message, context}\n" +
                                  "Files: Hourly rotation (logs_yyyyMMdd_HH.jsonl)\n" +
                                  "Consultation: Use a JSON parser or text editor to read hourly files.";
                Files.write(Paths.get(docFile), content.getBytes());
            } catch (IOException e) {
                System.err.println("Failed to create log documentation: " + e.getMessage());
            }
        }
    }

}
