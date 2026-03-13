package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.util.UUID;

public record CreateDeathRecordResult (UUID id, UUID pondsId, String pondName, LocalDate recordDate, int deathCount, String notes){
}
