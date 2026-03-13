package org.harvest.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record HarvestRecord(UUID id,String pondsName, UUID pondsId, LocalDate harvestDate, UUID organizationId, int harvestFishCount, BigDecimal totalWeight, String notes
) {


    public HarvestRecord update(LocalDate harvestDate, int harvestFishCount, BigDecimal totalWeight, String notes) {
        return new HarvestRecord(id, pondsName,pondsId, harvestDate, organizationId, harvestFishCount, totalWeight, notes);
    }

    public HarvestRecord update(UUID pondsId, String pondsName, LocalDate harvestDate, int harvestFishCount, BigDecimal totalWeight, String notes) {
        return new HarvestRecord(id, pondsName, pondsId, harvestDate, organizationId, harvestFishCount, totalWeight, notes);
    }


}
