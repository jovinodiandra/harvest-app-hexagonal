package org.harvest.application.dto.result;

import org.harvest.domain.FinanceRecord;

import java.util.List;

public record ViewFinanceReportResult (List<FinanceRecord> data){
}
