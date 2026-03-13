package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;
import java.util.UUID;

public record ViewGrowthChartQuery(UserSession session, UUID pondsId,LocalDate startDate, LocalDate endDate) {
}
