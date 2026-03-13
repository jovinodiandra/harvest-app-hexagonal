package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewHarvestRecordQuery;
import org.harvest.application.dto.result.ViewHarvestRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewHarvestRecordUseCase extends AuthenticationUseCase<ViewHarvestRecordQuery, ViewHarvestRecordResult
        > {
    private final HarvestRecordRepository harvestRecordRepository;

    public ViewHarvestRecordUseCase(HarvestRecordRepository harvestRecordRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
    }

    @Override
    protected UserSession authenticate(ViewHarvestRecordQuery command) {
        return command.session();
    }

    @Override
    protected ViewHarvestRecordResult executeBusiness(ViewHarvestRecordQuery command, UserSession userSession) {
        int offset = (command.page() - 1) * command.limit();
        List<HarvestRecord> harvestRecords = harvestRecordRepository.findAllByOrganizationId(userSession.getOrganizationId(), offset, command.limit());
        return new ViewHarvestRecordResult(harvestRecords);
    }

    @Override
    protected void validateCommand(ViewHarvestRecordQuery command) {
        if (command.page() < 1) {
            throw new ValidationException("Page must be greater than 0");
        }
        if (command.limit() < 1) {
            throw new ValidationException("Limit must be greater than 0");
        }
    }
}
