package org.harvest.application.dto.value;

import org.harvest.shared.exception.ValidationException;

public enum FeedPriceStatus {
    ACTIVE("active"), NONACTIVE("nonactive");

    private final String description;

    FeedPriceStatus(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return this == ACTIVE ? "active" : "non active";
    }




    public String getDescription() {
        return description;
    }


    public static FeedPriceStatus fromString(String value) {

        for (FeedPriceStatus feedPriceStatus : FeedPriceStatus.values()) {
            if (feedPriceStatus.name().equalsIgnoreCase(value)) {
                return feedPriceStatus;

            }
        }
        throw new ValidationException("unknown status" + value);
    }
}