package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewDeathRecordQuery;
import org.harvest.application.dto.result.ViewDeathRecordResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.DeathRecordRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.DeathRecord;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewDeathRecordUseCase extends AuthenticationUseCase<ViewDeathRecordQuery, ViewDeathRecordResult> {

    private final DeathRecordRepository deathRecordRepository;
    private final PondsRepository pondsRepository;

    public ViewDeathRecordUseCase(DeathRecordRepository deathRecordRepository, PondsRepository pondsRepository) {
        this.deathRecordRepository = deathRecordRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(ViewDeathRecordQuery command) {
        return command.session();
    }

    @Override
    protected ViewDeathRecordResult executeBusiness(ViewDeathRecordQuery command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null) {
            throw new ValidationException("Ponds not found");
        }

        int offset = (command.page() - 1) * command.limit();
        List<DeathRecord> deathRecord = deathRecordRepository.findAllByPondsId(command.pondsId(), offset, command.limit());
        return new ViewDeathRecordResult(deathRecord);
    }

    @Override
    protected void validateCommand(ViewDeathRecordQuery command) {
        if (command.page() < 1) {
            throw new ValidationException("Page must be greater than 0");
        }

        if (command.limit() < 1) {
            throw new ValidationException("Limit must be greater than 0");
        }
    }
}
