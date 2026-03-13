package org.harvest.domain;

import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinanceRecord(UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes, UUID organizationId) {
    public FinanceRecord update(TransactionType transactionType,LocalDate transactionDate, BigDecimal amount, String category, String notes) {
        return new FinanceRecord(id, transactionType, transactionDate, amount, category, notes, organizationId);
    }
}
