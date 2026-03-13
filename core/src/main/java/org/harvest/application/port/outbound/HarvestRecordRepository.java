package org.harvest.application.port.outbound;

import org.harvest.domain.HarvestRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HarvestRecordRepository {
    UUID nextId();
    void save(HarvestRecord harvestRecord);
    HarvestRecord findById(UUID id);
    void delete(HarvestRecord harvestRecord);
    void update(HarvestRecord harvestRecord);
    List<HarvestRecord> findAllByPondsId(UUID pondsId, int offset, int limit);
    List<HarvestRecord> findAllByOrganizationId(UUID organizationId, int offset, int limit);

    /**
     * Laporan panen berdasarkan filter kolam dan periode (US-54).
     * @param organizationId wajib
     * @param pondId optional, null = semua kolam
     * @param startDate optional, null = tidak filter awal
     * @param endDate optional, null = tidak filter akhir
     */
    List<HarvestRecord> findReportByOrganizationId(UUID organizationId, UUID pondId, LocalDate startDate, LocalDate endDate);

}
