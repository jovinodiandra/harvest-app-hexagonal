package org.harvest.logging.adapter;

import org.harvest.application.port.outbound.LogManager;
import org.harvest.domain.Log;
import org.harvest.domain.LogCategory;
import org.harvest.domain.LogLevel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Simple LogManager adapter that prints to console.
 * Suitable for development, debugging, and learning.
 */
public class ConsoleLogAdapter implements LogManager {

    private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneId.systemDefault());

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    private static final int MAX_LOG_SIZE = 1000;
    private final ConcurrentLinkedDeque<Log> logStore = new ConcurrentLinkedDeque<>();

    @Override
    public void log(Log log) {
        storeLog(log);
        printToConsole(log);
    }

    @Override
    public void info(LogCategory category, String message, String source) {
        log(Log.info(category, message, source));
    }

    @Override
    public void warn(LogCategory category, String message, String source) {
        log(Log.warn(category, message, source));
    }

    @Override
    public void error(LogCategory category, String message, String source, Throwable ex) {
        log(Log.error(category, message, source, ex));
    }

    @Override
    public void info(LogCategory category, String message, String source, Map<String, Object> context) {
        log(Log.info(category, message, source).withContext(context));
    }

    @Override
    public void error(LogCategory category, String message, String source, Throwable ex, Map<String, Object> context) {
        log(Log.error(category, message, source, ex).withContext(context));
    }

    @Override
    public List<Log> findByLevel(LogLevel level, Instant from, Instant to, int limit) {
        return logStore.stream()
                .filter(log -> log.level() == level)
                .filter(log -> isWithinTimeRange(log, from, to))
                .limit(limit)
                .toList();
    }

    @Override
    public List<Log> findByCategory(LogCategory category, Instant from, Instant to, int limit) {
        return logStore.stream()
                .filter(log -> log.category() == category)
                .filter(log -> isWithinTimeRange(log, from, to))
                .limit(limit)
                .toList();
    }

    @Override
    public List<Log> findByUserId(UUID userId, int limit) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return logStore.stream()
                .filter(log -> userId.equals(log.userId()))
                .limit(limit)
                .toList();
    }

    @Override
    public List<Log> findByTaskId(String taskId, int limit) {
        if (taskId == null) {
            return Collections.emptyList();
        }
        return logStore.stream()
                .filter(log -> taskId.equals(log.taskId()))
                .limit(limit)
                .toList();
    }

    private void storeLog(Log log) {
        logStore.addFirst(log);
        while (logStore.size() > MAX_LOG_SIZE) {
            logStore.removeLast();
        }
    }

    private void printToConsole(Log log) {
        String color = getLevelColor(log.level());
        String levelStr = padRight(log.level().name(), 5);
        String categoryStr = padRight("[" + log.category() + "]", 16);
        String timestamp = FORMATTER.format(log.createdAt());

        StringBuilder sb = new StringBuilder();
        sb.append(GRAY).append(timestamp).append(RESET);
        sb.append(" ").append(color).append(levelStr).append(RESET);
        sb.append(" ").append(CYAN).append(categoryStr).append(RESET);
        sb.append(" ").append(log.message());

        if (log.taskId() != null) {
            sb.append(GRAY).append(" [task:").append(log.taskId()).append("]").append(RESET);
        }

        System.out.println(sb);

        if (log.context() != null && !log.context().isEmpty()) {
            System.out.println(GRAY + "  └─ context: " + formatContext(log.context()) + RESET);
        }

        if (log.stackTrace() != null) {
            System.out.println(RED + log.stackTrace() + RESET);
        }
    }

    private String getLevelColor(LogLevel level) {
        return switch (level) {
            case DEBUG -> GRAY;
            case INFO -> GREEN;
            case WARN -> YELLOW;
            case ERROR, FATAL -> RED;
        };
    }

    private String padRight(String s, int length) {
        if (s.length() >= length) {
            return s;
        }
        return s + " ".repeat(length - s.length());
    }

    private String formatContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return "{}";
        }

        List<String> entries = new ArrayList<>();
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            entries.add(entry.getKey() + "=" + entry.getValue());
        }

        return "{" + String.join(", ", entries) + "}";
    }

    private boolean isWithinTimeRange(Log log, Instant from, Instant to) {
        if (log.createdAt() == null) {
            return false;
        }
        if (from != null && log.createdAt().isBefore(from)) {
            return false;
        }
        if (to != null && log.createdAt().isAfter(to)) {
            return false;
        }
        return true;
    }
}
