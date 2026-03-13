package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFinanceRecordQuery;
import org.harvest.application.dto.result.ViewFinanceRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewFinanceRecordsUseCase extends AuthenticationUseCase<ViewFinanceRecordQuery, ViewFinanceRecordResult> {
    private final FinanceRecordRepository financeRecordRepository;

    public ViewFinanceRecordsUseCase(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    @Override
    protected UserSession authenticate(ViewFinanceRecordQuery query) {
        return query.session();
    }

    @Override
    protected ViewFinanceRecordResult executeBusiness(ViewFinanceRecordQuery query, UserSession userSession) {
        int offset = (query.page() - 1) * query.limit();
        List<FinanceRecord> records = financeRecordRepository.findByFilters(userSession.getOrganizationId(), query.transactionType(), query.startDate(), query.endDate(), offset, query.limit());
        return new ViewFinanceRecordResult(records);
    }

    @Override
    protected void validateCommand(ViewFinanceRecordQuery query) {
        if (query.startDate() != null && query.endDate() != null && query.startDate().isAfter(query.endDate())) {
            throw new ValidationException("Start date cannot be after end date");
        }
        if (query.limit() <= 0 ) {
            throw new ValidationException("Limit must be greater than 0");
        }
        if (query.page() <=0) {
            throw new ValidationException("Offset must be greater than 0");
        }
    }
}
