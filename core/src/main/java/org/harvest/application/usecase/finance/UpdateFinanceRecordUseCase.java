package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateFinanceRecordCommand;
import org.harvest.application.dto.result.UpdateFinanceRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateFinanceRecordUseCase extends AuthenticationUseCase<UpdateFinanceRecordCommand, UpdateFinanceRecordResult> {
    private final FinanceRecordRepository financeRecordRepository;

    public UpdateFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    @Override
    protected UserSession authenticate(UpdateFinanceRecordCommand command) {
        return command.session();
    }

    @Override
    protected UpdateFinanceRecordResult executeBusiness(UpdateFinanceRecordCommand command, UserSession userSession) {
        FinanceRecord financeRecord = financeRecordRepository.findById(command.financeRecordId());
        if (financeRecord == null) {
            throw new ValidationException("FinanceRecord not found");
        }
        FinanceRecord updateFinanceRecord = financeRecord.update(command.transactionType(), command.transactionDate(), command.amount(), command.category(), command.notes());
        financeRecordRepository.update(updateFinanceRecord);
        return new UpdateFinanceRecordResult(updateFinanceRecord.id(), updateFinanceRecord.transactionType(), updateFinanceRecord.amount(), updateFinanceRecord.category(), updateFinanceRecord.notes(), updateFinanceRecord.transactionDate(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(UpdateFinanceRecordCommand command) {
        if (command.financeRecordId() == null) {
            throw new ValidationException("FinanceRecord Id cannot be null");
        }

        if (command.transactionType() == null) {
            throw new ValidationException("Transaction Type cannot be null");
        }

        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount cannot be null and must be greater than 0");
        }

        if (command.category() == null || command.category().isBlank()) {
            throw new ValidationException("Category cannot be null or empty");
        }

        if (command.notes() == null || command.notes().isBlank()) {
            throw new ValidationException("Notes cannot be null or empty");
        }
    }
}
