package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteGrowthRecordCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.GrowthRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.GrowthRecord;
import org.harvest.shared.exception.ValidationException;

public class DeleteGrowthRecordUseCase extends AuthenticationUseCase<DeleteGrowthRecordCommand, DefaultResult> {
    private final GrowthRecordRepository growthRecordRepository;

    public DeleteGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository) {
        this.growthRecordRepository = growthRecordRepository;
    }

    @Override
    protected UserSession authenticate(DeleteGrowthRecordCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteGrowthRecordCommand command, UserSession userSession) {
        GrowthRecord growthRecord = growthRecordRepository.findById(command.growthRecordId());
        if (growthRecord == null){
            throw new ValidationException("GrowthRecord not found");
        }
        growthRecordRepository.delete(growthRecord);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteGrowthRecordCommand command) {
        if (command.growthRecordId() == null){
            throw new ValidationException("GrowthRecord Id cannot be null");
        }
    }
}
