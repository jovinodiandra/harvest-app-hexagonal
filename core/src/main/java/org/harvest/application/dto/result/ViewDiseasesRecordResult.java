package org.harvest.application.dto.result;

import org.harvest.domain.DiseasesRecord;

import java.util.List;

public record ViewDiseasesRecordResult(List<DiseasesRecord> diseasesRecords) {
}
