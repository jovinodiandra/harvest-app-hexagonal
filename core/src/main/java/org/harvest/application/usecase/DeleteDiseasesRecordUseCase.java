package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteDiseasesRecordCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DiseasesRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DiseasesRecord;
import org.harvest.shared.exception.ValidationException;

public class DeleteDiseasesRecordUseCase extends AuthenticationUseCase<DeleteDiseasesRecordCommand, DefaultResult> {
    private final DiseasesRecordRepository diseasesRecordRepository;

    public DeleteDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository) {
        this.diseasesRecordRepository = diseasesRecordRepository;
    }

    @Override
    protected UserSession authenticate(DeleteDiseasesRecordCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteDiseasesRecordCommand command, UserSession userSession) {
        DiseasesRecord diseasesRecord = diseasesRecordRepository.findById(command.id());
        if (diseasesRecord == null) {
            throw new ValidationException("Data penyakit ikan tidak ditemukan");
        }

        if (!diseasesRecord.organizationId().equals(userSession.getOrganizationId())) {
            throw new ValidationException("Data penyakit ikan tidak ditemukan");
        }

        diseasesRecordRepository.delete(diseasesRecord);

        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteDiseasesRecordCommand command) {
        if (command.id() == null) {
            throw new ValidationException("id is required");
        }
    }
}
