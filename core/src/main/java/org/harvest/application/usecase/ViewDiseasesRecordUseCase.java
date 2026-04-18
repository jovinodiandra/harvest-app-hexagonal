package org.harvest.application.usecase;

import org.harvest.application.dto.query.VIewDiseasesRecordQuery;
import org.harvest.application.dto.result.ViewDiseasesRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DiseasesRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DiseasesRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

import java.util.List;

public class ViewDiseasesRecordUseCase extends AuthenticationUseCase<VIewDiseasesRecordQuery, ViewDiseasesRecordResult> {
    private final DiseasesRecordRepository diseasesRecordRepository;
    private final PondsRepository pondsRepository;

    public ViewDiseasesRecordUseCase(DiseasesRecordRepository diseasesRecordRepository, PondsRepository pondsRepository) {
        this.diseasesRecordRepository = diseasesRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(VIewDiseasesRecordQuery command) {
        return command.session();
    }

    @Override
    protected ViewDiseasesRecordResult executeBusiness(VIewDiseasesRecordQuery command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }
        Pagination pagination = new Pagination(command.page(),command.limit());
        List<DiseasesRecord> diseasesRecordList = diseasesRecordRepository.findAllByPondsId(command.pondId(), pagination);
        return new ViewDiseasesRecordResult(diseasesRecordList);
    }

    @Override
    protected void validateCommand(VIewDiseasesRecordQuery command) {

    }
}
