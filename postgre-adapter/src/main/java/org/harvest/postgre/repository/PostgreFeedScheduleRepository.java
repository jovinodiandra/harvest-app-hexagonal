package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.FeedScheduleRepository;
import org.harvest.domain.FeedSchedule;
import org.harvest.postgre.config.DatabaseConfig;
import org.harvest.shared.query.Pagination;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreFeedScheduleRepository implements FeedScheduleRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, feed_type, feed_amount, feed_time, organization_id FROM feed_schedule WHERE id = ?";
    private static final String SELECT_BY_ORGANIZATION = "SELECT id, ponds_id, feed_type, feed_amount, feed_time, organization_id FROM feed_schedule WHERE organization_id = ? ORDER BY feed_time LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO feed_schedule (id, ponds_id, feed_type, feed_amount, feed_time, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE feed_schedule SET feed_type = ?, feed_amount = ?, feed_time = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM feed_schedule WHERE id = ?";

    private final DataSource dataSource;

    public PostgreFeedScheduleRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreFeedScheduleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(FeedSchedule feedSchedule) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, feedSchedule.id());
            ps.setObject(2, feedSchedule.pondsId());
            ps.setString(3, feedSchedule.feedType());
            ps.setBigDecimal(4, feedSchedule.feedAmount());
            ps.setObject(5, feedSchedule.feedTime());
            ps.setObject(6, feedSchedule.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save feed schedule", e);
        }
    }

    @Override
    public FeedSchedule findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find feed schedule by id", e);
        }
    }

    @Override
    public void delete(FeedSchedule feedSchedule) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, feedSchedule.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete feed schedule", e);
        }
    }

    @Override
    public void update(FeedSchedule feedSchedule) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, feedSchedule.feedType());
            ps.setBigDecimal(2, feedSchedule.feedAmount());
            ps.setObject(3, feedSchedule.feedTime());
            ps.setObject(4, feedSchedule.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update feed schedule", e);
        }
    }

    @Override
    public List<FeedSchedule> findByOrganizationId(UUID organizationId, Pagination pagination) {
        List<FeedSchedule> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setObject(2,pagination);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find feed schedules by organization", e);
        }
        return list;
    }

    private static FeedSchedule mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        String feedType = rs.getString("feed_type");
        BigDecimal feedAmount = rs.getBigDecimal("feed_amount");
        Time t = rs.getTime("feed_time");
        LocalTime feedTime = t != null ? t.toLocalTime() : null;
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new FeedSchedule(id, pondsId, feedType, feedAmount, feedTime, organizationId);
    }
}
