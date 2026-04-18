package org.harvest.application.port.outbound;

import java.time.Duration;
import java.time.Instant;

public interface TaskScheduler {

    String schedule(Runnable task, Instant executeAt);

    String schedule(String taskId, Runnable task, Instant executeAt);

    String scheduleRecurring(Runnable task, Duration interval);

    String scheduleRecurring(Runnable task, String cronExpression);

    boolean reschedule(String taskId, Instant newExecuteAt);

    boolean update(String taskId, Runnable newTask, Instant newExecuteAt);

    boolean cancel(String taskId);

    int cancelByPrefix(String taskIdPrefix);

    boolean exists(String taskId);

    TaskStatus getStatus(String taskId);

    Instant getScheduledTime(String taskId);

    enum TaskStatus {
        SCHEDULED,
        RUNNING,
        COMPLETED,
        CANCELLED,
        FAILED,
        NOT_FOUND
    }
}
