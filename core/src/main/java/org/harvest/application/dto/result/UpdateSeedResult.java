package org.harvest.application.dto.result;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateSeedResult (UUID id, UUID pondsId, String pondName, String type, int quantity, LocalDate stockDate){
}
