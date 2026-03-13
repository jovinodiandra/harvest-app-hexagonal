package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateDiseasesRecordCommand;
import org.harvest.application.dto.result.UpdateDiseasesRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DiseasesRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DiseasesRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class UpdateDiseasesRecordUseCase extends AuthenticationUseCase<UpdateDiseasesRecordCommand, UpdateDiseasesRecordResult> {
    private final DiseasesRecordRepository diseasesRecordRepository;
    private final PondsRepository pondsRepository;

    public UpdateDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        this.diseasesRecordRepository = diseasesRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateDiseasesRecordCommand command) {
        return command.session();
    }

    @Override
    protected UpdateDiseasesRecordResult executeBusiness(UpdateDiseasesRecordCommand command, UserSession userSession) {
        DiseasesRecord diseasesRecord = diseasesRecordRepository.findById(command.id());
        if (diseasesRecord == null) {
            throw new ValidationException("Data penyakit ikan tidak ditemukan");
        }

        if (!diseasesRecord.organizationId().equals(userSession.getOrganizationId())) {
             throw new ValidationException("Data penyakit ikan tidak ditemukan");
        }

        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null) {
            throw new ValidationException("Kolam tidak ditemukan");
        }

        if (!ponds.organizationId().equals(userSession.getOrganizationId())) {
             throw new ValidationException("Kolam tidak ditemukan");
        }

        DiseasesRecord updated = diseasesRecord.update(
                command.pondId(),
                command.diseaseName(),
                command.symptoms(),
                command.infectedFishCount(),
                command.diseaseDate(),
                command.notes()
        );

        diseasesRecordRepository.update(updated);

        return new UpdateDiseasesRecordResult(
                updated.id(),
                updated.pondsId(),
                updated.diseaseName(),
                updated.symptoms(),
                updated.infectedFishCount(),
                updated.diseasesDate(),
                updated.notes()
        );
    }

    @Override
    protected void validateCommand(UpdateDiseasesRecordCommand command) {
        if (command.id() == null) {
            throw new ValidationException("id is required");
        }
        if (command.pondId() == null) {
            throw new ValidationException("pond_id is required");
        }
        if (command.diseaseName() == null || command.diseaseName().isBlank()) {
            throw new ValidationException("disease_name cannot be empty");
        }
        if (command.symptoms() == null || command.symptoms().isBlank()) {
            throw new ValidationException("symptoms cannot be empty");
        }
        if (command.infectedFishCount() < 0) {
            throw new ValidationException("invalid infected_fish_count: must be greater than or equal to 0");
        }
        if (command.diseaseDate() == null || command.diseaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("disease_date cannot be in the future");
        }
    }
}
