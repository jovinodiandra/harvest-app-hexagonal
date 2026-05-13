package org.harvest.application.dto.command;

import org.harvest.application.dto.value.TransactionType;
import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateFinanceRecordCommand (UserSession session, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes){
}
