package org.harvest.application.dto.result;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateFeedScheduleResult (UUID id, UUID pondsId, String pondName, String feedType, BigDecimal feedAmount, LocalTime feedTime){
}
