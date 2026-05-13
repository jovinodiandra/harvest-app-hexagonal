package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateWatterQualityCommand;
import org.harvest.application.dto.result.CreateWatterQualityResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.WatterQualityRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Ponds;
import org.harvest.domain.WatterQuality;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class CreateWatterQualityUseCase extends AuthenticationUseCase<CreateWatterQualityCommand, CreateWatterQualityResult> {
    private final WatterQualityRepository watterQualityRepository;
    private final PondsRepository pondsRepository;

    public CreateWatterQualityUseCase(WatterQualityRepository watterQualityRepository, PondsRepository pondsRepository) {
        this.watterQualityRepository = watterQualityRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateWatterQualityCommand command) {
        return command.session();
    }

    @Override
    protected CreateWatterQualityResult executeBusiness(CreateWatterQualityCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        WatterQuality watterQuality = new WatterQuality(watterQualityRepository.nextId(), command.pondsId(), command.recordDate(),command.ph(), command.temperature(),command.dissolvedOxygen(), userSession.getOrganizationId());
        watterQualityRepository.save(watterQuality);
        return new CreateWatterQualityResult(watterQuality.id(), watterQuality.pondsID(), ponds.name(), watterQuality.recordDate(), watterQuality.ph(), watterQuality.temperature(), watterQuality.dissolvedOxygen());
    }

    @Override
    protected void validateCommand(CreateWatterQualityCommand command) {
        if (command.pondsId() == null) {
            throw new ValidationException("Ponds Id cannot be null");
        }

        if (command.recordDate() == null || command.recordDate().isAfter(LocalDate.now())) {
            throw new ValidationException("invalid record date");
        }

        if (command.ph() == null){
            throw new ValidationException("ph cannot be null");
        }

        if (command.temperature() == null){
            throw new ValidationException("temperature cannot be null");
        }

        if (command.dissolvedOxygen() == null){
            throw new ValidationException("dissolvedOxygen cannot be null");
        }



    }
}
