package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteFinanceRecordCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;
import org.harvest.shared.exception.ValidationException;

public class DeleteFinanceRecordUseCase extends AuthenticationUseCase<DeleteFinanceRecordCommand, DefaultResult> {
    private final FinanceRecordRepository financeRecordRepository;

    public DeleteFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    @Override
    protected UserSession authenticate(DeleteFinanceRecordCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteFinanceRecordCommand command, UserSession userSession) {
        FinanceRecord financeRecord = financeRecordRepository.findById(command.id());
        if (financeRecord == null) {
            throw new ValidationException("Finance Record not found");
        }
        financeRecordRepository.delete(financeRecord);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteFinanceRecordCommand command) {
        if (command.id() == null) {
            throw new ValidationException("Finance Record Id cannot be null");
        }
    }
}
