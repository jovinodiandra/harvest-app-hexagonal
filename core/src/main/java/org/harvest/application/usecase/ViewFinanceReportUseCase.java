package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFinanceReportQuery;
import org.harvest.application.dto.result.ViewFinanceReportResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;

import java.util.List;

public class ViewFinanceReportUseCase extends AuthenticationUseCase<ViewFinanceReportQuery, ViewFinanceReportResult> {

    private final FinanceRecordRepository financeRecordRepository;

    public ViewFinanceReportUseCase(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    @Override
    protected UserSession authenticate(ViewFinanceReportQuery command) {
        return command.session();
    }

    @Override
    protected ViewFinanceReportResult executeBusiness(ViewFinanceReportQuery command, UserSession userSession) {
        List<FinanceRecord> financeRecords = financeRecordRepository.findAllByOrganizationId(userSession.getOrganizationId(), command.startDate(), command.endDate());
        return new ViewFinanceReportResult(financeRecords);
    }

    @Override
    protected void validateCommand(ViewFinanceReportQuery command) {
        if (command.startDate() != null && command.endDate() != null && command.startDate().isAfter(command.endDate())) {
            throw new org.harvest.shared.exception.ValidationException("date start cannot be after date end");
        }
    }
}
