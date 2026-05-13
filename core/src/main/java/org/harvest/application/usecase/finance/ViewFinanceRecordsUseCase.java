package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFinanceRecordQuery;
import org.harvest.application.dto.result.ViewFinanceRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

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
        Pagination pagination = new Pagination(query.page(), query.limit());
        List<FinanceRecord> records = financeRecordRepository.findByFilters(userSession.getOrganizationId(), query.transactionType(), query.startDate(), query.endDate(), pagination);
        return new ViewFinanceRecordResult(records);
    }

    @Override
    protected void validateCommand(ViewFinanceRecordQuery query) {
        if (query.startDate() != null && query.endDate() != null && query.startDate().isAfter(query.endDate())) {
            throw new ValidationException("Start date cannot be after end date");
        }

    }
}
