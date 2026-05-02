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
    private final UUID organizationId;
    private final UUID deletedBy;
    private final LocalDateTime deletedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public FeedPrice(UUID id, FeedName feedName, Price pricePerKiloGram, LocalDate effectiveDate, FeedPriceStatus status, String description, UUID organizationId, UUID deletedBy, LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.feedName = feedName;
        this.pricePerKiloGram = pricePerKiloGram;
        this.effectiveDate = effectiveDate;
        this.status = status;
        this.description = description;
        this.organizationId = organizationId;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FeedPrice create(UUID id, FeedName feedName, Price pricePerKiloGram, LocalDate effectiveDate, String description, UUID organizationId) {
        return new FeedPrice(id,
                feedName,
                pricePerKiloGram,
                effectiveDate,
                FeedPriceStatus.ACTIVE,
                description,
                organizationId,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now());
    }


    public FeedPrice deactivate(UUID userId, LocalDateTime now) {
        if (this.status != FeedPriceStatus.ACTIVE) {
            throw new ValidationException("Only active feed price can be deactivated");
        }

        return new FeedPrice(
                id,
                feedName,
                pricePerKiloGram,
                effectiveDate,
                FeedPriceStatus.NONACTIVE,
                description,
                organizationId,
                userId,
                now,
                this.createdAt,
                now
        );
    }

    public FeedPrice update(FeedName feedName, Price price, LocalDate effectiveDate, String description) {
        if (this.status == FeedPriceStatus.NONACTIVE) {
            throw new ValidationException("cannot update deleted feed price");
        }
        return new FeedPrice(
                this.id,
                feedName,
                price,
                effectiveDate,
                this.status,
                description,
                this.organizationId,
                deletedBy,
                deletedAt,
                this.createdAt,
                this.updatedAt);
    }


    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return this.organizationId;
    }

    public FeedName getFeedName() {
        return this.feedName;
    }

    public Price getPricePerKiloGram() {
        return this.pricePerKiloGram;
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

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


}
