package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewFishStatisticQuery;
import org.harvest.application.dto.result.ViewFishStatisticResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FishStatisticsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FishStatistics;

import java.util.List;

public class ViewFishStatisticUseCase extends AuthenticationUseCase<ViewFishStatisticQuery, ViewFishStatisticResult> {
    private final FishStatisticsRepository fishStatisticsRepository;

    public ViewFishStatisticUseCase(FishStatisticsRepository fishStatisticsRepository) {
        this.fishStatisticsRepository = fishStatisticsRepository;
    }

    @Override
    protected UserSession authenticate(ViewFishStatisticQuery command) {
        return command.session();
    }

    @Override
    protected ViewFishStatisticResult executeBusiness(ViewFishStatisticQuery command, UserSession userSession) {
        List<FishStatistics> fishStatistics = fishStatisticsRepository.reportFishStatistics(userSession.getOrganizationId(),command.startDate(),command.endDate());
        return new ViewFishStatisticResult(fishStatistics);
    }

    @Override
    protected void validateCommand(ViewFishStatisticQuery command) {

    }
}
