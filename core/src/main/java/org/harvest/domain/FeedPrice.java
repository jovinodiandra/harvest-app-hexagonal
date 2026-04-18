package org.harvest.domain;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;
import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FeedPrice {
    private final UUID id;
    private final FeedName feedName;
    private final Price pricePerKiloGram;
    private final LocalDate effectiveDate;
    private final FeedPriceStatus status;
    private final String description;
    private final LocalDateTime createdAt;
    private final UUID organizationId;
    private final UUID deletedBy;
    private final LocalDateTime deletedAt;

    public FeedPrice(UUID id, FeedName feedName, Price pricePerKiloGram, LocalDate effectiveDate, FeedPriceStatus status, String description, LocalDateTime createdAt, UUID organizationId, UUID deletedBy, LocalDateTime deletedAt) {
        this.id = id;
        this.feedName = feedName;
        this.pricePerKiloGram = pricePerKiloGram;
        this.effectiveDate = effectiveDate;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.organizationId = organizationId;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    public static FeedPrice create(UUID id, FeedName feedName, Price pricePerKiloGram, String description, LocalDate effectiveDate, UUID organizationId) {
        return new FeedPrice(id,
                feedName,
                pricePerKiloGram,
                effectiveDate,
                FeedPriceStatus.ACTIVE,
                description,
                LocalDateTime.now(),
                organizationId,
                null,
                null);
    }

    public  FeedPrice markAsDeleted(UUID userId){
        return new FeedPrice(this.id,
                this.feedName,
                this.pricePerKiloGram,
                this.effectiveDate,
                FeedPriceStatus.NONACTIVE,
                description,
                this.createdAt,
                organizationId,
                userId,
                LocalDateTime.now());
    }

    public FeedPrice update(FeedName feedName, Price price, LocalDate effectiveDate, String description) {
        if (this.status == FeedPriceStatus.NONACTIVE){
            throw new ValidationException("cannot update deleted feed price");
        }
        return new FeedPrice(
                this.id,
                feedName,
                price,
                effectiveDate,
                FeedPriceStatus.ACTIVE,
                description,
                this.createdAt,
                this.organizationId, deletedBy, deletedAt);
    }


    public UUID getId() {
        return id;
    }

    public String getFeedName() {
        return feedName.getValue();
    }

    public BigDecimal getPricePerKiloGram() {
        return pricePerKiloGram.getValue();
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public FeedPriceStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
