package org.harvest.logging.adapter;

import org.harvest.application.port.outbound.LogManager;
import org.harvest.domain.Log;
import org.harvest.application.dto.value.LogCategory;
import org.harvest.application.dto.value.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * LogManager adapter using SLF4J/Logback.
 * Logs are stored in memory for querying (demo purpose).
 * For production, use database or ELK stack.
 */
public class Slf4jLogAdapter implements LogManager {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jLogAdapter.class);
    private static final int MAX_LOG_SIZE = 10000;

    private final ConcurrentLinkedDeque<Log> logStore = new ConcurrentLinkedDeque<>();

    @Override
    public void log(Log log) {
        storeLog(log);
        writeToSlf4j(log);
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

    private void writeToSlf4j(Log log) {
        String formattedMessage = formatLogMessage(log);

        switch (log.level()) {
            case DEBUG -> logger.debug(formattedMessage);
            case INFO -> logger.info(formattedMessage);
            case WARN -> logger.warn(formattedMessage);
            case ERROR, FATAL -> {
                if (log.stackTrace() != null) {
                    logger.error(formattedMessage + "\n" + log.stackTrace());
                } else {
                    logger.error(formattedMessage);
                }
            }
        }
    }

    private String formatLogMessage(Log log) {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(log.category()).append("]");
        sb.append(" ").append(log.message());
        sb.append(" | source=").append(log.source());

        if (log.taskId() != null) {
            sb.append(" | taskId=").append(log.taskId());
        }

        if (log.userId() != null) {
            sb.append(" | userId=").append(log.userId());
        }

        if (log.organizationId() != null) {
            sb.append(" | orgId=").append(log.organizationId());
        }

        if (log.context() != null && !log.context().isEmpty()) {
            sb.append(" | context=").append(formatContext(log.context()));
        }

        return sb.toString();
    }

    private String formatContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        List<String> entries = new ArrayList<>();

        for (Map.Entry<String, Object> entry : context.entrySet()) {
            entries.add(entry.getKey() + "=" + entry.getValue());
        }

        sb.append(String.join(", ", entries));
        sb.append("}");

        return sb.toString();
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
