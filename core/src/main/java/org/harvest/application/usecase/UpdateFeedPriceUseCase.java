package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateFeedPriceCommand;
import org.harvest.application.dto.result.UpdateFeedPriceResult;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;
import java.util.Optional;

public class UpdateFeedPriceUseCase extends AuthenticationUseCase<UpdateFeedPriceCommand, UpdateFeedPriceResult> {

    private final FeedPriceRepository feedPriceRepository;

    public UpdateFeedPriceUseCase(FeedPriceRepository feedPriceRepository) {
        this.feedPriceRepository = feedPriceRepository;
    }

    @Override
    protected UserSession authenticate(UpdateFeedPriceCommand command) {
        return command.session();
    }

    @Override
    protected UpdateFeedPriceResult executeBusiness(UpdateFeedPriceCommand command, UserSession userSession) {
        Optional<FeedPrice> byId = feedPriceRepository.findById(command.id());
        if (userSession.getRole() != Role.OWNER || userSession.getRole() != Role.ADMIN) {
            throw new ValidationException("role must be owner or admin");
        }
        FeedName feedName = new FeedName(command.feedName());
        Price pricePerKiloGram = new Price(command.pricePerKiloGram());

        FeedPrice update = byId.orElseThrow().update(feedName, pricePerKiloGram, command.effectiveDate(), command.description());
        feedPriceRepository.update(update);
        return new UpdateFeedPriceResult(update.getId(), update.getFeedName().getValue(), update.getPricePerKiloGram().getValue(), update.getEffectiveDate(), update.getStatus(), update.getDescription(), update.getCreatedAt());
    }

    @Override
    protected void validateCommand(UpdateFeedPriceCommand command) {
        if (command.feedName() == null || command.feedName().isBlank()) {
            throw new ValidationException("feed name is required");
        }
        if (command.pricePerKiloGram() == null) {
            throw new ValidationException("price is required");
        }

        if (command.description() == null || command.description().isBlank()) {
            throw new ValidationException("reason is required");
        }

        if (command.effectiveDate() == null) {
            throw new ValidationException("effective date is required");
        }

        if (command.effectiveDate().isAfter(LocalDate.now())) {
            throw new ValidationException("effective date cannot be in the future");
        }
    }
}
