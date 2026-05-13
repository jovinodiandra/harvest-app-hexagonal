package org.harvest.application.dto.command;

import org.harvest.application.dto.value.TransactionType;
import org.harvest.application.port.outbound.security.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateFinanceRecordCommand (UserSession session, UUID financeRecordId, TransactionType transactionType, BigDecimal amount, String category, String notes, LocalDate transactionDate){
}
