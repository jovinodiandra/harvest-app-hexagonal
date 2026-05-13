package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewHarvestRecordQuery;
import org.harvest.application.dto.result.ViewHarvestRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

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
        Pagination pagination = new Pagination(command.page(),command.limit());
        List<HarvestRecord> harvestRecords = harvestRecordRepository.findAllByOrganizationId(userSession.getOrganizationId(), pagination);
        return new ViewHarvestRecordResult(harvestRecords);
    }

    @Override
    protected void validateCommand(ViewHarvestRecordQuery command) {

    }
}
