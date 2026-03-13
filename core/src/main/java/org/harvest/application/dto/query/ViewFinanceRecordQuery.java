package org.harvest.application.dto.query;

import org.harvest.application.dto.value.TransactionType;
import org.harvest.application.port.outbound.security.UserSession;

import java.time.LocalDate;

public record ViewFinanceRecordQuery(
        UserSession session,
        TransactionType transactionType,
        LocalDate startDate,
        LocalDate endDate,
        int page,
        int limit
) {
}
