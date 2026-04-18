package org.harvest.domain.value;

import org.harvest.shared.exception.ValidationException;

import java.math.BigDecimal;

public class Price {
    private static final BigDecimal MAX = new BigDecimal("50000");
    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null) {
            throw new ValidationException("price is required");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("price must be than greater 0");
        }

        if (value.compareTo(MAX) >= 0) {
            throw new ValidationException("price must be below 50000");

        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
