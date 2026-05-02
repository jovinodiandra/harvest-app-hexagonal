package org.harvest.application.usecase.feedschedule;

import org.harvest.application.dto.command.CreateFeedScheduleCommand;
import org.harvest.application.dto.result.CreateFeedScheduleResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedSchedule;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;

public class CreateFeedScheduleUseCase extends AuthenticationUseCase<CreateFeedScheduleCommand, CreateFeedScheduleResult> {
    private final FeedScheduleRepository feedScheduleRepository;
    private final PondsRepository pondsRepository;

    public CreateFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository, PondsRepository pondsRepository) {
        this.feedScheduleRepository = feedScheduleRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(CreateFeedScheduleCommand command) {
        return command.session();
    }

    @Override
    protected CreateFeedScheduleResult executeBusiness(CreateFeedScheduleCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        FeedSchedule feedSchedule = new FeedSchedule(feedScheduleRepository.nextId(), command.pondsId(), command.feedType(), command.feedAmount(), command.feedTime(), userSession.getOrganizationId());
        feedScheduleRepository.save(feedSchedule);
        return new CreateFeedScheduleResult(feedSchedule.id(), feedSchedule.pondsId(), ponds.name(), command.feedType(), command.feedAmount(), command.feedTime());
    }

    @Override
    protected void validateCommand(CreateFeedScheduleCommand command) {
        if (command.pondsId() == null) {
            throw new ValidationException("Pond Id Cannot be null");
        }

        if (command.feedType() == null || command.feedType().isBlank()) {
            throw new ValidationException("Feed Type Cannot be null");
        }

        if (command.feedAmount() == null|| command.feedAmount().compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new ValidationException("Feed Amount Cannot be null and must be greater than 0");
        }

        if (command.feedTime() == null) {
            throw new ValidationException("Feed Time Cannot be null");
        }

    }
}
