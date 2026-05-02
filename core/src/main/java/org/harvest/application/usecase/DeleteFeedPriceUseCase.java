package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteFeedPriceCommand;
import org.harvest.application.dto.result.DeleteFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedPrice;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class DeleteFeedPriceUseCase extends AuthenticationUseCase<DeleteFeedPriceCommand, DeleteFeedPriceResult> {
    private final FeedPriceRepository feedPriceRepository;

    public DeleteFeedPriceUseCase(FeedPriceRepository feedPriceRepository) {
        this.feedPriceRepository = feedPriceRepository;

    }

    @Override
    protected UserSession authenticate(DeleteFeedPriceCommand command) {
        return command.session();
    }

    @Override
    protected DeleteFeedPriceResult executeBusiness(DeleteFeedPriceCommand command, UserSession userSession) {
        FeedPrice feedPrice = feedPriceRepository.findById(command.id()).
                                                 orElseThrow(() -> new ValidationException("Feed price not found"));

        if (!(userSession.getRole() == Role.OWNER || userSession.getRole() == Role.ADMIN)) {
            throw new ValidationException("role must be owner or admin");
        }

        LocalDateTime now = LocalDateTime.now();
        FeedPrice delete = feedPrice.deactivate(userSession.getUserId(), now, command.description());
        feedPriceRepository.delete(delete);
        return new DeleteFeedPriceResult(
                feedPrice.getId(),
                feedPrice.getFeedName().getValue(),
                feedPrice.getPricePerKiloGram().getValue(),
                feedPrice.getEffectiveDate(),
                feedPrice.getStatus(),
                feedPrice.getDescription(),
                now,
                userSession.getUserId());
    }

    @Override
    protected void validateCommand(DeleteFeedPriceCommand command) {

        if (command.description() == null) {
            throw new ValidationException("description is required");
        }

        if (command.description().length() < 10) {
            throw new ValidationException("description must be than greater 10 character");
        }

    }
}
