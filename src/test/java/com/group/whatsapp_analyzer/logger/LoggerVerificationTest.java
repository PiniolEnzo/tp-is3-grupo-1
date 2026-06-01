package com.group.whatsapp_analyzer.logger;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoggerVerificationTest {
    private static final String TEST_LOGS_DIR = "logs-test";
    private static final String TEST_DOC_FILE = TEST_LOGS_DIR + "/logs_info.txt";

    @BeforeAll
    static void globalSetup() {
        System.setProperty("app.logs.dir", TEST_LOGS_DIR);
    }

    @BeforeEach
    void setup() throws Exception {
        resetSingleton();
        deleteDirectory(new File(TEST_LOGS_DIR));
        Files.createDirectories(Paths.get(TEST_LOGS_DIR));
    }

    @AfterEach
    void teardown() throws IOException {
        deleteDirectory(new File(TEST_LOGS_DIR));
    }

    private void resetSingleton() throws Exception {
        java.lang.reflect.Field instance = Logger.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] allContents = directory.listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    deleteDirectory(file);
                }
            }
            directory.delete();
        }
    }

    @Test
    void testJsonSchema() throws IOException {
        Logger logger = Logger.getInstance();
        String level = "INFO";
        String message = "Test message";
        String context = "Test context";
        
        logger.log(level, message, context);
        
        File dir = new File(TEST_LOGS_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".jsonl"));
        
        assertNotNull(files);
        assertEquals(1, files.length);
        
        String content = Files.readString(files[0].toPath());
        assertTrue(content.contains("\"timestamp\":"), "Missing timestamp");
        assertTrue(content.contains("\"level\":\"" + level + "\""), "Missing or incorrect level");
        assertTrue(content.contains("\"message\":\"" + message + "\""), "Missing or incorrect message");
        assertTrue(content.contains("\"context\":\"" + context + "\""), "Missing or incorrect context");
    }

    @Test
    void testRetention() throws IOException {
        Logger logger = Logger.getInstance();
        logger.log("INFO", "old log", "context");
        
        File dir = new File(TEST_LOGS_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".jsonl"));
        assertNotNull(files);
        
        File oldFile = files[0];
        FileTime oldTime = FileTime.from(Instant.now().minus(Duration.ofHours(10)));
        Files.setLastModifiedTime(oldFile.toPath(), oldTime);
        
        LogCleanupTask cleanupTask = new LogCleanupTask();
        cleanupTask.cleanup();
        
        assertFalse(oldFile.exists(), "Old log file should have been deleted");
    }

    @Test
    void testDocumentation() {
        Logger logger = Logger.getInstance();
        logger.log("INFO", "trigger", "context");
        
        File docFile = new File(TEST_DOC_FILE);
        assertTrue(docFile.exists(), "Documentation file should exist");
        
        try {
            String content = Files.readString(docFile.toPath());
            assertTrue(content.contains("Log System Documentation"), "Missing title");
            assertTrue(content.contains("Schema: {timestamp, level, message, context}"), "Missing schema info");
        } catch (IOException e) {
            fail("Could not read doc file");
        }
    }

    @Test
    void testReliability() throws IOException {
        deleteDirectory(new File(TEST_LOGS_DIR));
        Files.writeString(Paths.get(TEST_LOGS_DIR), "I am a file, not a directory");
        
        Logger logger = Logger.getInstance();
        
        assertDoesNotThrow(() -> {
            logger.log("INFO", "this should fail gracefully", "context");
        });
    }
}
