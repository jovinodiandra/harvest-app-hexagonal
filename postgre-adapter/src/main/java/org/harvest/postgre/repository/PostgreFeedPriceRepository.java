package org.harvest.postgre.repository;

import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.port.outbound.FeedPriceRepository;
import org.harvest.domain.FeedPrice;
import org.harvest.domain.value.FeedName;
import org.harvest.domain.value.Price;
import org.harvest.postgre.config.DatabaseConfig;
import org.harvest.shared.query.Pagination;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgreFeedPriceRepository implements FeedPriceRepository {
    private final String SELECT_BY_ID =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at, updated_at  " +
                    "FROM feed_price WHERE id = ? AND deleted_at IS NULL";

    private final String SELECT_BY_FEED_NAME_AND_STATUS =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    " organization_id, deleted_by, deleted_at,created_at,updated_at " +
                    "FROM feed_price WHERE name = ? AND status = ? AND deleted_at IS NULL";

    private final String SELECT_BY_STATUS =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "  organization_id, deleted_by, deleted_at,created_at,updated_at " +
                    "FROM feed_price WHERE status = ? AND deleted_at IS NULL " +
                    "ORDER BY effective_date DESC";

    // Query 4: Select all with pagination
    private final String SELECT_ALL =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at, updated_at " +
                    "FROM feed_price WHERE organization_id = ? AND deleted_at IS NULL " +
                    "ORDER BY created_at DESC LIMIT ? OFFSET ?";

    // Query 5: Count total records untuk pagination
    private final String COUNT_ALL =
            "SELECT COUNT(*) FROM feed_price WHERE organization_id = ? AND deleted_at IS NULL";

    private static final String INSERT = "INSERT INTO public.feed_price (id, name, price_per_kilo_gram, effective_date, status, " +
            "description, organization_id) " +
            "VALUES (?, ?, ?, ?, ?::feed_price_status_enum, ?, ?) ";

    private static final String UPDATE =
            "UPDATE feed_price SET name = ?, price_per_kilo_gram = ?, effective_date = ?, " +
                    "status = ?, description = ?,deleted_by = ?, deleted_at = ?,  updated_at = ? WHERE id = ? AND deleted_at IS NULL";


    private final String SOFT_DELETE =
            "UPDATE feed_price SET deleted_by = ?, deleted_at = ? WHERE id = ?";


    private final String EXISTS_ACTIVE_PRICE =
            "SELECT EXISTS(SELECT 1 FROM feed_price WHERE name = ? AND status = 'ACTIVE' " +
                    "AND effective_date <= CURRENT_DATE AND deleted_at IS NULL)";


    private final String SELECT_BY_ORGANIZATION =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at,updated_at " +
                    "FROM feed_price WHERE organization_id = ? AND deleted_at IS NULL " +
                    "ORDER BY effective_date DESC LIMIT ? OFFSET ?";


    private final String SELECT_ACTIVE_CURRENT =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at, updated_at " +
                    "FROM feed_price WHERE status = 'ACTIVE' AND effective_date <= CURRENT_DATE " +
                    "AND deleted_at IS NULL ORDER BY effective_date DESC";


    private final String SELECT_BY_DATE_RANGE =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at,updated_at " +
                    "FROM feed_price WHERE effective_date BETWEEN ? AND ? " +
                    "AND organization_id = ? AND deleted_at IS NULL " +
                    "ORDER BY effective_date DESC";


    private final String SELECT_LATEST_BY_FEED_NAME =
            "SELECT id, name, price_per_kilo_gram, effective_date, status, description, " +
                    "organization_id, deleted_by, deleted_at, created_at , updated_at" +
                    "FROM feed_price WHERE name = ? AND deleted_at IS NULL " +
                    "ORDER BY effective_date DESC LIMIT 1";


//    private final String UPDATE_EXPIRED_STATUS =
//            "UPDATE feed_price SET status = 'EXPIRED' WHERE status = 'ACTIVE' " +
//                    "AND effective_date < CURRENT_DATE AND deleted_at IS NULL";


    private final DataSource dataSource;

    public PostgreFeedPriceRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }


    public PostgreFeedPriceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void update(FeedPrice feedPrice) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(UPDATE);
            ps.setString(1, feedPrice.getFeedName().getValue());
            ps.setBigDecimal(2, feedPrice.getPricePerKiloGram().getValue());
            ps.setObject(3, feedPrice.getEffectiveDate());
            ps.setString(4, feedPrice.getStatus().getDescription());
            ps.setString(5, feedPrice.getDescription());
            ps.setObject(6, feedPrice.getDeletedBy());
            ps.setObject(7, feedPrice.getDeletedAt());
            ps.setObject(8, feedPrice.getUpdatedAt());
            ps.setObject(9, feedPrice.getId());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Feed price not found ");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed update to feed price", e);
        }
    }

//    @Override
//    public void softDeleted(UUID id, UUID deletedBy, LocalDateTime deletedAt) {
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement ps = connection.prepareStatement(SOFT_DELETE);
//
//            ps.setObject(1, deletedBy);
//            ps.setObject(2, deletedAt);
//            ps.setObject(3, id);
//            int affectedRows = ps.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new RuntimeException("Feed price not found or already deleted");
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed deleted to feed price", e);
//        }
//
//
//    }

    @Override
    public Optional<FeedPrice> findById(UUID id) {

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID);
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed find by id to feed price", e);
        }
    }

    @Override
    public Optional<FeedPrice> findByFeedNameAndStatus(FeedName feedName, FeedPriceStatus feedPriceStatus) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_FEED_NAME_AND_STATUS);
            ps.setObject(1, feedName.getValue());
            ps.setObject(2, feedPriceStatus.name());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("failed find by status and name  to feed price", e);
        }

    }

    @Override
    public void save(FeedPrice feedPrice) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(INSERT);
            ps.setObject(1, feedPrice.getId());
            ps.setString(2, feedPrice.getFeedName().getValue());
            ps.setBigDecimal(3, feedPrice.getPricePerKiloGram().getValue());
            ps.setDate(4, Date.valueOf(feedPrice.getEffectiveDate()));
            ps.setString(5, feedPrice.getStatus().name());
            ps.setString(6, feedPrice.getDescription());
            ps.setObject(7, feedPrice.getOrganizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save feed price", e);
        }
    }

    @Override
    public List<FeedPrice> findAllByOrganizationId(UUID uuid, Pagination pagination) {
        ArrayList<FeedPrice> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ORGANIZATION);
            ps.setObject(1, uuid);
            ps.setObject(2, pagination.getLimit());
            ps.setObject(3, pagination.offset());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public boolean existByFeedNameAndStatus(FeedName feedName, FeedPriceStatus feedPriceStatus) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(EXISTS_ACTIVE_PRICE);
            ps.setObject(1, feedName.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static FeedPrice mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        FeedName name = new FeedName(rs.getString("name"));
        Price pricePerKg = new Price(rs.getBigDecimal("price_per_kilo_gram"));
        LocalDate effectiveDate = rs.getDate("effective_date").toLocalDate();
        FeedPriceStatus status = FeedPriceStatus.valueOf(rs.getString("status"));
        String description = rs.getString("description");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        UUID deletedBy = (UUID) rs.getObject("deleted_by");
        var deletedTs = rs.getTimestamp("deleted_at");
        LocalDateTime deletedAt = deletedTs != null ? deletedTs.toLocalDateTime() : null;
        var createdTs = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdTs != null ? createdTs.toLocalDateTime() : null;
        var updatedTs = rs.getTimestamp("updated_at");
        LocalDateTime updatedAt = updatedTs != null ? updatedTs.toLocalDateTime() : null;
        return new FeedPrice(
                id,
                name,
                pricePerKg,
                effectiveDate,
                status, description,
                organizationId,
                deletedBy,
                deletedAt,
                createdAt,
                updatedAt);

    }
}
