package org.harvest.postgre.repository;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;
import org.harvest.shared.query.Pagination;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgreFeedPriceRepository implements FeedPriceRepository {
    private final String SELECT_BY_ID = "";

    private final DataSource dataSource;

    public PostgreFeedPriceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return null;
    }

    @Override
    public void update(FeedPrice feedPrice) {

    }

    @Override
    public void delete(FeedPrice feedPrice) {

    }

    @Override
    public Optional<FeedPrice> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<FeedPrice> findByFeedNameAndStatus(String s, FeedPriceStatus feedPriceStatus) {
        return Optional.empty();
    }

    @Override
    public void save(FeedPrice feedPrice) {

    }

    @Override
    public List<FeedPrice> findAll(UUID uuid, Pagination pagination) {
        return null;
    }

    @Override
    public List<FeedPrice> findByStatus(FeedPriceStatus feedPriceStatus) {
        return null;
    }

    @Override
    public boolean existActivePrice(String s) {
        return false;
    }

    public static FeedPrice mapRow() throws SQLException{
//        private final UUID id;
//        private final FeedName feedName;
//        private final Price pricePerKiloGram;
//        private final LocalDate effectiveDate;
//        private final FeedPriceStatus status;
//        private final String description;
//        private final LocalDateTime createdAt;
//        private final UUID organizationId;
//        private final UUID deletedBy;
//        private final LocalDateTime deletedAt;
        return  null;
    }
}
