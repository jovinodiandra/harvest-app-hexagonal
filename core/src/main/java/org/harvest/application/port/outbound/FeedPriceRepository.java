package org.harvest.application.port.outbound;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.shared.query.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedPriceRepository {

    UUID nextId();

    void update(FeedPrice feedPrice);

//    void softDeleted(UUID id, UUID deletedBy, LocalDateTime deletedAt);

    Optional<FeedPrice> findById (UUID id);

    Optional<FeedPrice> findByFeedNameAndStatus(FeedName name, FeedPriceStatus status);

    void save(FeedPrice feedPrice);

    List<FeedPrice> findAllByOrganizationId(UUID organizationId, Pagination pagination);

//    List<FeedPrice> findByStatus(FeedPriceStatus status);

    boolean existByFeedNameAndStatus(FeedName feedName, FeedPriceStatus status);
}
