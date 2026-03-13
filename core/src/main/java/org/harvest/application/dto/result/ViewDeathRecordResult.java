package org.harvest.application.dto.result;

import org.harvest.domain.DeathRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ViewDeathRecordResult (List<DeathRecord> deathRecords){
}
