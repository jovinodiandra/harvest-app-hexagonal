package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.FeedReminderRepository;
import org.harvest.domain.FeedReminder;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreFeedReminderRepository implements FeedReminderRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, reminder_date, reminder_time, feed_type, notes, organization_id FROM feed_reminder WHERE id = ?";
    private static final String SELECT_BY_FILTERS = "SELECT id, ponds_id, reminder_date, reminder_time, feed_type, notes, organization_id FROM feed_reminder WHERE organization_id = ? AND (?::uuid IS NULL OR ponds_id = ?) AND (?::date IS NULL OR reminder_date = ?) ORDER BY reminder_date, reminder_time";
    private static final String INSERT = "INSERT INTO feed_reminder (id, ponds_id, reminder_date, reminder_time, feed_type, notes, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE feed_reminder SET ponds_id = ?, reminder_date = ?, reminder_time = ?, feed_type = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM feed_reminder WHERE id = ?";

    private final DataSource dataSource;

    public PostgreFeedReminderRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreFeedReminderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(FeedReminder feedReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, feedReminder.id());
            ps.setObject(2, feedReminder.pondsId());
            ps.setObject(3, feedReminder.reminderDate());
            ps.setObject(4, feedReminder.reminderTime());
            ps.setString(5, feedReminder.feedType());
            ps.setString(6, feedReminder.notes());
            ps.setObject(7, feedReminder.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save feed reminder", e);
        }
    }

    @Override
    public void delete(FeedReminder feedReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, feedReminder.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete feed reminder", e);
        }
    }

    @Override
    public FeedReminder findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find feed reminder by id", e);
        }
    }

    @Override
    public List<FeedReminder> findByFilters(UUID organizationId, UUID pondId, LocalDate date) {
        List<FeedReminder> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_FILTERS)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, pondId);
            ps.setObject(3, pondId);
            ps.setObject(4, date);
            ps.setObject(5, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find feed reminders by filters", e);
        }
        return list;
    }

    @Override
    public void update(FeedReminder feedReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, feedReminder.pondsId());
            ps.setObject(2, feedReminder.reminderDate());
            ps.setObject(3, feedReminder.reminderTime());
            ps.setString(4, feedReminder.feedType());
            ps.setString(5, feedReminder.notes());
            ps.setObject(6, feedReminder.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update feed reminder", e);
        }
    }

    private static FeedReminder mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        LocalDate reminderDate = rs.getObject("reminder_date", LocalDate.class);
        Time t = rs.getTime("reminder_time");
        LocalTime reminderTime = t != null ? t.toLocalTime() : null;
        String feedType = rs.getString("feed_type");
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new FeedReminder(id, pondsId, reminderDate, reminderTime, feedType, notes, organizationId);
    }
}
