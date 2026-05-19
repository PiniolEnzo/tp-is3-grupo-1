package com.group.whatsapp_analyzer.logger;

import java.util.Objects;

public class LogEntry {
    private final String timestamp;
    private final String level;
    private final String message;
    private final String context;

    public LogEntry(String timestamp, String level, String message, String context) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.context = context;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntry logEntry = (LogEntry) o;
        return Objects.equals(timestamp, logEntry.timestamp) &&
               Objects.equals(level, logEntry.level) &&
               Objects.equals(message, logEntry.message) &&
               Objects.equals(context, logEntry.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, level, message, context);
    }

    @Override
    public String toString() {
        return "LogEntry{" +
               "timestamp='" + timestamp + '\'' +
               ", level='" + level + '\'' +
               ", message='" + message + '\'' +
               ", context='" + context + '\'' +
               '}';
    }
}
