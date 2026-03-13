package org.harvest.application.dto.result;

import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateFinanceRecordResult (UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes){
}
