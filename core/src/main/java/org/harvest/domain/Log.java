package org.harvest.domain;

import org.harvest.application.dto.value.LogCategory;
import org.harvest.application.dto.value.LogLevel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Log(
        UUID id,
        LogLevel level,
        LogCategory category,
        String message,
        String source,
        UUID userId,
        UUID organizationId,
        String taskId,
        Map<String, Object> context,
        String stackTrace,
        Instant createdAt
) {
    public static Log info(LogCategory category, String message, String source) {
        return new Log(
                UUID.randomUUID(), LogLevel.INFO, category, message, source,
                null, null, null, Map.of(), null, Instant.now()
        );
    }

    public static Log warn(LogCategory category, String message, String source) {
        return new Log(
                UUID.randomUUID(), LogLevel.WARN, category, message, source,
                null, null, null, Map.of(), null, Instant.now()
        );
    }

    public static Log error(LogCategory category, String message, String source, Throwable ex) {
        return new Log(
                UUID.randomUUID(), LogLevel.ERROR, category, message, source,
                null, null, null, Map.of(), getStackTraceString(ex), Instant.now()
        );
    }

    public Log withUser(UUID userId, UUID organizationId) {
        return new Log(
                id, level, category, message, source,
                userId, organizationId, taskId, context, stackTrace, createdAt
        );
    }

    public Log withTaskId(String taskId) {
        return new Log(
                id, level, category, message, source,
                userId, organizationId, taskId, context, stackTrace, createdAt
        );
    }

    public Log withContext(Map<String, Object> context) {
        return new Log(
                id, level, category, message, source,
                userId, organizationId, taskId, context, stackTrace, createdAt
        );
    }

    private static String getStackTraceString(Throwable ex) {
        if (ex == null) return null;
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
