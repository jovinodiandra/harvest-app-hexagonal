package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateFeedPriceCommand;
import org.harvest.application.dto.result.CreateFeedPriceResult;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;

public class CreateFeedPriceUseCase extends AuthenticationUseCase<CreateFeedPriceCommand, CreateFeedPriceResult> {
    private final FeedPriceRepository feedPriceRepository;

    public CreateFeedPriceUseCase(FeedPriceRepository feedPriceRepository) {
        this.feedPriceRepository = feedPriceRepository;
    }

    @Override
    protected UserSession authenticate(CreateFeedPriceCommand command) {
        return command.session();
    }

    @Override
    protected CreateFeedPriceResult executeBusiness(CreateFeedPriceCommand command, UserSession userSession) {
        if (!(userSession.getRole() == Role.OWNER || userSession.getRole() == Role.ADMIN)) {
            throw new ValidationException("role must be owner and admin");
        }

        FeedName feedName = new FeedName(command.feedName());
        Price price = new Price(command.pricePerKiloGram());
        FeedPrice feedPrice = FeedPrice.create(
                feedPriceRepository.nextId(),
                feedName,
                price,
                command.description(),
                command.effectiveDate(),
                userSession.getOrganizationId());
        feedPriceRepository.save(feedPrice);
        return new CreateFeedPriceResult(
                feedPrice.getId(),
                feedPrice.getFeedName(),
                feedPrice.getPricePerKiloGram(),
                feedPrice.getEffectiveDate(),
                feedPrice.getStatus(),
                feedPrice.getDescription(),
                feedPrice.getCreatedAt(),
                feedPrice.getUpdatedAt());
    }

    @Override
    protected void validateCommand(CreateFeedPriceCommand command) {

        if (command.feedName() == null || command.feedName().isBlank()) {
            throw new ValidationException("feed name is required");
        }

        if (command.pricePerKiloGram() == null) {
            throw new ValidationException("price is required");
        }

        if (command.effectiveDate() == null){
            throw new ValidationException("effective date is required");
        }

        if (command.effectiveDate().isAfter(LocalDate.now())){
            throw new ValidationException("effective date cannot be in the future");
        }

    }
}
