package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.UpdateFinanceRecordResult;
import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateFinanceRecordResponse(UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes, LocalDateTime updatedAt) {

    public static UpdateFinanceRecordResponse from(UpdateFinanceRecordResult result) {
        return new UpdateFinanceRecordResponse(result.id(), result.transactionType(), result.transactionDate(), result.amount(), result.category(), result.notes(), result.updatedAt());
    }
}
