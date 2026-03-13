package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewFinanceReportResult;
import org.harvest.application.dto.value.TransactionType;
import org.harvest.domain.FinanceRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewFinanceReportResponse(List<FinanceReportItem> records, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance) {

    public static ViewFinanceReportResponse from(ViewFinanceReportResult result) {
        List<FinanceReportItem> items = result.data().stream()
                .map(FinanceReportItem::from)
                .toList();
        
        BigDecimal totalIncome = result.data().stream()
                .filter(r -> r.transactionType() == TransactionType.INCOME)
                .map(FinanceRecord::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpense = result.data().stream()
                .filter(r -> r.transactionType() == TransactionType.EXPENSE)
                .map(FinanceRecord::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal balance = totalIncome.subtract(totalExpense);
        
        return new ViewFinanceReportResponse(items, totalIncome, totalExpense, balance);
    }

    public record FinanceReportItem(UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes) {
        public static FinanceReportItem from(FinanceRecord record) {
            return new FinanceReportItem(record.id(), record.transactionType(), record.transactionDate(), record.amount(), record.category(), record.notes());
        }
    }
}
