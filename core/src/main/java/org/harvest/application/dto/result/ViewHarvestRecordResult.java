package org.harvest.application.dto.result;

import org.harvest.domain.HarvestRecord;

import java.util.List;

public record ViewHarvestRecordResult(List<HarvestRecord> harvestRecords
) {
}
