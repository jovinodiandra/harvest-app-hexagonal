package org.harvest.application.port.outbound;

import org.harvest.application.dto.value.TransactionType;
import org.harvest.domain.FinanceRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FinanceRecordRepository {
    UUID nextId();
    void save(FinanceRecord financeRecord);
    void delete(FinanceRecord financeRecord);
    void update(FinanceRecord financeRecord);
    FinanceRecord findById(UUID id);
    List<FinanceRecord> findByFilters(UUID organizationId, TransactionType transactionType, LocalDate startDate, LocalDate endDate, int offset, int limit);
    List<FinanceRecord> findAllByOrganizationId(UUID organizationId, LocalDate startDate, LocalDate endDate);
}
