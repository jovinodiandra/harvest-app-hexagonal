package org.harvest.application.port.outbound;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.shared.query.Pagination;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedPriceRepository {

    UUID nextId();

    void update(FeedPrice feedPrice);

    void delete(FeedPrice feedPrice);

    Optional<FeedPrice> findById (UUID id);

    Optional<FeedPrice> findByFeedNameAndStatus(FeedName name, FeedPriceStatus status);

    void save(FeedPrice feedPrice);

    List<FeedPrice> findAllByOrganizationId(UUID organizationId, Pagination pagination);

//    List<FeedPrice> findByStatus(FeedPriceStatus status);

    boolean existByFeedNameAndStatus(FeedName feedName, FeedPriceStatus status);
}
