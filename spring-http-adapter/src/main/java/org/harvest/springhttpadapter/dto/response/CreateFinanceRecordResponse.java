package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.CreateFinanceRecordResult;
import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateFinanceRecordResponse(UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes) {

    public static CreateFinanceRecordResponse from(CreateFinanceRecordResult result) {
        return new CreateFinanceRecordResponse(result.id(), result.transactionType(), result.transactionDate(), result.amount(), result.category(), result.notes());
    }
}
