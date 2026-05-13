package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateHarvestRecordCommand;
import org.harvest.application.dto.result.UpdateHarvestRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UpdateHarvestRecordUseCase extends AuthenticationUseCase<UpdateHarvestRecordCommand, UpdateHarvestRecordResult> {

    private final HarvestRecordRepository harvestRecordRepository;
    private final PondsRepository pondsRepository;

    public UpdateHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository, PondsRepository pondsRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateHarvestRecordCommand command) {
        return command.session();
    }

    @Override
    protected UpdateHarvestRecordResult executeBusiness(UpdateHarvestRecordCommand command, UserSession userSession) {
        HarvestRecord existing = harvestRecordRepository.findById(command.id());
        if (existing == null) {
            throw new ValidationException("Data panen tidak ditemukan");
        }
        if (!existing.organizationId().equals(userSession.getOrganizationId())) {
            throw new ValidationException("Data panen tidak ditemukan");
        }
        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null) {
            throw new ValidationException("Kolam tidak ditemukan");
        }
        if (!ponds.organizationId().equals(userSession.getOrganizationId())) {
            throw new ValidationException("Kolam tidak ditemukan");
        }
        HarvestRecord updated = existing.update(command.pondId(), ponds.name(), command.harvestDate(), command.harvestedFishCount(), command.totalWeight(), command.notes());
        harvestRecordRepository.update(updated);
        return new UpdateHarvestRecordResult(
                updated.id(),
                updated.pondsId(),
                updated.harvestDate(),
                updated.harvestFishCount(),
                updated.totalWeight(),
                updated.notes(),
                LocalDateTime.now()
        );
    }

    @Override
    protected void validateCommand(UpdateHarvestRecordCommand command) {
        if (command.id() == null) {
            throw new ValidationException("ID data panen tidak boleh kosong");
        }
        if (command.pondId() == null) {
            throw new ValidationException("Kolam tidak boleh kosong");
        }
        if (command.harvestDate() == null || command.harvestDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Tanggal panen tidak valid atau tidak boleh di masa depan");
        }
        if (command.harvestedFishCount() < 0) {
            throw new ValidationException("Jumlah ikan harus lebih dari atau sama dengan 0");
        }
        if (command.totalWeight() == null || command.totalWeight().signum() < 0) {
            throw new ValidationException("Total berat harus lebih dari atau sama dengan 0");
        }
    }
}
