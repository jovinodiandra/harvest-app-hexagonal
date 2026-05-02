package org.harvest.application.usecase.finance;

import org.harvest.application.dto.command.CreateFinanceRecordCommand;
import org.harvest.application.dto.result.CreateFinanceRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FinanceRecord;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateFinanceRecordUseCase extends AuthenticationUseCase<CreateFinanceRecordCommand, CreateFinanceRecordResult> {
    private final FinanceRecordRepository financeRecordRepository;

    public CreateFinanceRecordUseCase(FinanceRecordRepository financeRecordRepository) {
        this.financeRecordRepository = financeRecordRepository;
    }

    @Override
    protected UserSession authenticate(CreateFinanceRecordCommand command) {
        return command.session();
    }

    @Override
    protected CreateFinanceRecordResult executeBusiness(CreateFinanceRecordCommand command, UserSession userSession) {
        FinanceRecord financeRecord = new FinanceRecord(financeRecordRepository.nextId(), command.transactionType(), command.transactionDate(), command.amount(), command.category(), command.notes(), userSession.getOrganizationId());
        financeRecordRepository.save(financeRecord);
        return new CreateFinanceRecordResult(financeRecord.id(), financeRecord.transactionType(), financeRecord.transactionDate(), financeRecord.amount(), financeRecord.category(), financeRecord.notes());
    }

    @Override
    protected void validateCommand(CreateFinanceRecordCommand command) {
        if (command.transactionType() == null) {
            throw new ValidationException("Transaction Type cannot be null");
        }
        if (command.transactionDate() == null || command.transactionDate().isAfter(LocalDate.now())) {
            throw new ValidationException("invalid transaction date");
        }
        if (command.amount().compareTo(BigDecimal.ZERO) <= 0) {
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
