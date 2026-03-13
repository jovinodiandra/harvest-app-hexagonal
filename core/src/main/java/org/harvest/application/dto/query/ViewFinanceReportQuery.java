package org.harvest.application.dto.query;

import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;

public record ViewFinanceReportQuery (UserSession session, LocalDate startDate, LocalDate endDate){
}
