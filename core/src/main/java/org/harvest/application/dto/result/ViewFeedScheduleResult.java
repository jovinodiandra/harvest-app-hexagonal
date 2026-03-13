package org.harvest.application.dto.result;

import org.harvest.domain.FeedSchedule;

import java.util.List;

public record ViewFeedScheduleResult(List<FeedSchedule> feedSchedules) {
}
