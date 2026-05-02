package org.harvest.springhttpadapter.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateFeedPriceRequest(
        String feedName,

        BigDecimal pricePerKiloGram,

        LocalDate effectiveDate,

        String description)
{

}
