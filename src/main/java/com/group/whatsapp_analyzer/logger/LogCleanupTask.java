package com.group.whatsapp_analyzer.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;
import java.time.Instant;
import java.time.Duration;

public class LogCleanupTask {
    private final String logsDir;
    private static final long RETENTION_HOURS = 8;

    public LogCleanupTask() {
        this.logsDir = System.getProperty("app.logs.dir", "logs");
    }

    public void cleanup() {
        try (Stream<Path> files = Files.list(Paths.get(logsDir))) {
            Instant threshold = Instant.now().minus(Duration.ofHours(RETENTION_HOURS));
            
            files.filter(path -> path.getFileName().toString().endsWith(".json"))
                 .filter(path -> {
                     try {
                         return Files.getLastModifiedTime(path).toInstant().isBefore(threshold);
                     } catch (Exception e) {
                         return false;
                     }
                 })
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         System.err.println("Failed to delete old log file: " + path + " - " + e.getMessage());
                     }
                 });
        } catch (Exception e) {
            System.err.println("Cleanup task failed: " + e.getMessage());
        }
    }
}
