package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateFeedScheduleCommand;
import org.harvest.application.dto.result.UpdateFeedScheduleResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedSchedule;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalTime;

public class UpdateFeedScheduleUseCase extends AuthenticationUseCase<UpdateFeedScheduleCommand, UpdateFeedScheduleResult> {
    private final FeedScheduleRepository feedScheduleRepository;
    private final PondsRepository pondsRepository;

    public UpdateFeedScheduleUseCase(FeedScheduleRepository feedScheduleRepository, PondsRepository pondsRepository) {
        this.feedScheduleRepository = feedScheduleRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateFeedScheduleCommand command) {
        return command.session();
    }

    @Override
    protected UpdateFeedScheduleResult executeBusiness(UpdateFeedScheduleCommand command, UserSession userSession) {
        FeedSchedule feedSchedule = feedScheduleRepository.findById(command.feedId());
        if (feedSchedule == null){
            throw new ValidationException("FeedSchedule not found");
        }
        Ponds ponds = pondsRepository.findById(command.pondsId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        FeedSchedule updateFeedSchedule = feedSchedule.update(command.feedType(), command.feedAmount(), command.feedTime());
        feedScheduleRepository.update(updateFeedSchedule);
        return new UpdateFeedScheduleResult(feedSchedule.id(), feedSchedule.pondsId(), ponds.name(), command.feedType(),command.feedAmount(), command.feedTime() );
    }

    @Override
    protected void validateCommand(UpdateFeedScheduleCommand command) {

        if (command.pondsId() == null){
            throw new ValidationException("Pond Id Cannot be null");
        }

        if (command.feedType() == null|| command.feedType().isBlank()) {
            throw new ValidationException("Feed Type Cannot be empty");
        }

        if (command.feedAmount() == null || command.feedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Feed Amount Cannot be null and must be greater than 0");
        }

        if (command.feedTime() == null) {
            throw new ValidationException("Feed Time Cannot be null");
        }
    }
}
