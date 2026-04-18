package org.harvest.application.port.outbound;

import org.harvest.domain.Log;
import org.harvest.domain.LogCategory;
import org.harvest.domain.LogLevel;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface LogManager {

    void log(Log log);

    void info(LogCategory category, String message, String source);

    void warn(LogCategory category, String message, String source);

    void error(LogCategory category, String message, String source, Throwable ex);

    void info(LogCategory category, String message, String source, Map<String, Object> context);

    void error(LogCategory category, String message, String source, Throwable ex, Map<String, Object> context);

    List<Log> findByLevel(LogLevel level, Instant from, Instant to, int limit);

    List<Log> findByCategory(LogCategory category, Instant from, Instant to, int limit);

    List<Log> findByUserId(UUID userId, int limit);

    List<Log> findByTaskId(String taskId, int limit);
}
