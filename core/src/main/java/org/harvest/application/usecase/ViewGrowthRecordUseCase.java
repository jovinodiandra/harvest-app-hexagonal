package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewGrowthRecordQuery;
import org.harvest.application.dto.result.ViewGrowthRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.GrowthRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.GrowthRecord;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewGrowthRecordUseCase extends AuthenticationUseCase<ViewGrowthRecordQuery, ViewGrowthRecordResult> {

    private final GrowthRecordRepository growthRecordRepository;

    public ViewGrowthRecordUseCase(GrowthRecordRepository growthRecordRepository) {
        this.growthRecordRepository = growthRecordRepository;
    }

    @Override
    protected UserSession authenticate(ViewGrowthRecordQuery command) {
        return command.session();
    }

    @Override
    protected ViewGrowthRecordResult executeBusiness(ViewGrowthRecordQuery command, UserSession userSession) {
        int offset = (command.page() - 1) * command.limit();
        List<GrowthRecord> growthRecordsFindByIdAndOrganizationId = growthRecordRepository.findAllByPondsIdAndOrganizationId(command.pondsId(), userSession.getOrganizationId(), offset, command.limit());
        return new ViewGrowthRecordResult( growthRecordsFindByIdAndOrganizationId);
    }

    @Override
    protected void validateCommand(ViewGrowthRecordQuery command) {
        if (command.page() < 1) {
            throw new ValidationException("Page must be greater than 0");
        }
        if (command.limit() < 1) {
            throw new ValidationException("Limit must be greater than 0");
        }
    }
}
