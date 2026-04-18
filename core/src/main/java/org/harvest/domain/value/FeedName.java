package org.harvest.domain.value;

import org.harvest.shared.exception.ValidationException;

public class FeedName {
    private final String value;

    public FeedName(String value) {
        if (value == null || value.isBlank()){
            throw new ValidationException("feed name is required");
        }

        if (value.length() < 3){
            throw new ValidationException("feed name must be more than 3 characters");
        }

        if (value.length() > 100){
            throw new ValidationException("feed name must not be more than 100 characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
