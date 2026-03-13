package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewFinanceRecordResult;
import org.harvest.application.dto.value.TransactionType;
import org.harvest.domain.FinanceRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewFinanceRecordResponse(List<FinanceRecordItem> records) {

    public static ViewFinanceRecordResponse from(ViewFinanceRecordResult result) {
        List<FinanceRecordItem> items = result.data().stream()
                .map(FinanceRecordItem::from)
                .toList();
        return new ViewFinanceRecordResponse(items);
    }

    public record FinanceRecordItem(UUID id, TransactionType transactionType, LocalDate transactionDate, BigDecimal amount, String category, String notes) {
        public static FinanceRecordItem from(FinanceRecord record) {
            return new FinanceRecordItem(record.id(), record.transactionType(), record.transactionDate(), record.amount(), record.category(), record.notes());
        }
    }
}
