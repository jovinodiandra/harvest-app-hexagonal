package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewHarvestReportQuery;
import org.harvest.application.dto.result.ViewHarvestReportResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;

import java.util.List;

public class ViewHarvestReportUseCase extends AuthenticationUseCase<ViewHarvestReportQuery, ViewHarvestReportResult> {

    private final HarvestRecordRepository harvestRecordRepository;

    public ViewHarvestReportUseCase(HarvestRecordRepository harvestRecordRepository) {
        this.harvestRecordRepository = harvestRecordRepository;
    }

    @Override
    protected UserSession authenticate(ViewHarvestReportQuery command) {
        return command.session();
    }

    @Override
    protected ViewHarvestReportResult executeBusiness(ViewHarvestReportQuery command, UserSession userSession) {
        List<HarvestRecord> data = harvestRecordRepository.findReportByOrganizationId(
                userSession.getOrganizationId(),
                command.pondId(),
                command.startDate(),
                command.endDate()
        );
        return new ViewHarvestReportResult(data);
    }

    @Override
    protected void validateCommand(ViewHarvestReportQuery command) {
        if (command.startDate() != null && command.endDate() != null && command.startDate().isAfter(command.endDate())) {
            throw new org.harvest.shared.exception.ValidationException("Tanggal mulai tidak boleh setelah tanggal akhir");
        }
    }
}
