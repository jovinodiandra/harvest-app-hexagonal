package org.harvest.application.dto.result;

import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateFinanceRecordResult(UUID id, TransactionType transactionType, BigDecimal amount, String category, String notes, LocalDate transactionDate, LocalDateTime updatedAt) {
}
