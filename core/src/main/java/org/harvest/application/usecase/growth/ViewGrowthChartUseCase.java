package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewGrowthChartQuery;
import org.harvest.application.dto.result.ViewGrowthChartResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.GrowthChartRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.GrowthChart;

import java.util.List;

public class ViewGrowthChartUseCase extends AuthenticationUseCase<ViewGrowthChartQuery, ViewGrowthChartResult> {
    private final GrowthChartRepository growthChartRepository;

    public ViewGrowthChartUseCase(GrowthChartRepository growthChartRepository) {
        this.growthChartRepository = growthChartRepository;
    }

    @Override
    protected UserSession authenticate(ViewGrowthChartQuery command) {
        return command.session();
    }

    @Override
    protected ViewGrowthChartResult executeBusiness(ViewGrowthChartQuery command, UserSession userSession) {
        List<GrowthChart> growthChart = growthChartRepository.reportGrowthChart(userSession.getOrganizationId(),command.pondsId(),command.startDate(),command.endDate());
        return new ViewGrowthChartResult(growthChart);
    }

    @Override
    protected void validateCommand(ViewGrowthChartQuery command) {

    }
}
