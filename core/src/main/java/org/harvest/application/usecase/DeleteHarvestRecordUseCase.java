package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteHarvestRecordCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.shared.exception.ValidationException;

public class DeleteHarvestRecordUseCase extends AuthenticationUseCase<DeleteHarvestRecordCommand, DefaultResult> {

    private final HarvestRecordRepository harvestRecordRepository;

    public DeleteHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
    }

    @Override
    protected UserSession authenticate(DeleteHarvestRecordCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteHarvestRecordCommand command, UserSession userSession) {
        HarvestRecord harvestRecord = harvestRecordRepository.findById(command.id());
        if (harvestRecord == null) {
            throw new ValidationException("Data panen tidak ditemukan");
        }
        if (!harvestRecord.organizationId().equals(userSession.getOrganizationId())) {
            throw new ValidationException("Data panen tidak ditemukan");
        }
        harvestRecordRepository.delete(harvestRecord);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteHarvestRecordCommand command) {
        if (command.id() == null) {
            throw new ValidationException("ID data panen tidak boleh kosong");
        }
    }
}
