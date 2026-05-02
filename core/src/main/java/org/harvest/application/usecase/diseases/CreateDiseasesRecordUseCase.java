package org.harvest.application.usecase.diseases;

import org.harvest.application.dto.command.CreateDiseasesRecordCommand;
import org.harvest.application.dto.result.CreateDiseasesRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DiseasesRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DiseasesRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class CreateDiseasesRecordUseCase extends AuthenticationUseCase<CreateDiseasesRecordCommand, CreateDiseasesRecordResult> {
    private final DiseasesRecordRepository diseasesRecordRepository;
    private final PondsRepository pondsRepository;

    public CreateDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        this.diseasesRecordRepository = diseasesRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateDiseasesRecordCommand command) {
        return command.session();
    }

    @Override
    protected CreateDiseasesRecordResult executeBusiness(CreateDiseasesRecordCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }

        DiseasesRecord diseasesRecord = new DiseasesRecord(
                diseasesRecordRepository.nextId(),
                command.pondId(),
                command.diseaseName(),
                command.symptoms(),
                command.infectedFishCount(),
                command.diseaseDate(),
                command.notes(),
                userSession.getOrganizationId()
        );

        diseasesRecordRepository.save(diseasesRecord);

        return new CreateDiseasesRecordResult(
                diseasesRecord.id(),
                diseasesRecord.pondsId(),
                diseasesRecord.diseaseName(),
                diseasesRecord.symptoms(),
                diseasesRecord.infectedFishCount(),
                diseasesRecord.diseasesDate(),
                diseasesRecord.notes()
        );
    }

    @Override
    protected void validateCommand(CreateDiseasesRecordCommand command) {
        if (command.pondId() == null) {
            throw new ValidationException("pond_id is required");
        }
        if (command.diseaseName() == null || command.diseaseName().isBlank()) {
            throw new ValidationException("disease cannot be empty");
        }
        if (command.symptoms() == null || command.symptoms().isBlank()) {
            throw new ValidationException("symptoms cannot be empty");
        }
        if (command.infectedFishCount() < 0) {
            throw new ValidationException("invalid infected_fish_count: must be greater than or equal to 0");
        }
        if (command.diseaseDate() == null || command.diseaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("disasese_date cannot be in the future");
        }
    }
}
