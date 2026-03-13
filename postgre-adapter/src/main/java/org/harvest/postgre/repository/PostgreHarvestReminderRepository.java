package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.HarvestReminderRepository;
import org.harvest.domain.HarvestReminder;
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

public class PostgreHarvestReminderRepository implements HarvestReminderRepository {

    private static final String SELECT_BY_ID = "SELECT id, pond_id, reminder_date, reminder_time, notes, organization_id FROM harvest_reminder WHERE id = ?";
    private static final String SELECT_BY_FILTERS = "SELECT id, pond_id, reminder_date, reminder_time, notes, organization_id FROM harvest_reminder WHERE organization_id = ? AND (?::uuid IS NULL OR pond_id = ?) AND (?::date IS NULL OR reminder_date = ?) ORDER BY reminder_date, reminder_time";
    private static final String INSERT = "INSERT INTO harvest_reminder (id, pond_id, reminder_date, reminder_time, notes, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE harvest_reminder SET reminder_date = ?, reminder_time = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM harvest_reminder WHERE id = ?";

    private final DataSource dataSource;

    public PostgreHarvestReminderRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreHarvestReminderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(HarvestReminder harvestReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, harvestReminder.id());
            ps.setObject(2, harvestReminder.pondId());
            ps.setObject(3, harvestReminder.reminderDate());
            ps.setObject(4, harvestReminder.reminderTime());
            ps.setString(5, harvestReminder.notes());
            ps.setObject(6, harvestReminder.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save harvest reminder", e);
        }
    }

    @Override
    public void delete(HarvestReminder harvestReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, harvestReminder.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete harvest reminder", e);
        }
    }

    @Override
    public HarvestReminder findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find harvest reminder by id", e);
        }
    }

    @Override
    public List<HarvestReminder> findByFilters(UUID organizationId, UUID pondId, LocalDate date) {
        List<HarvestReminder> list = new ArrayList<>();
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
            throw new RuntimeException("Failed to find harvest reminders by filters", e);
        }
        return list;
    }

    @Override
    public void update(HarvestReminder harvestReminder) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, harvestReminder.reminderDate());
            ps.setObject(2, harvestReminder.reminderTime());
            ps.setString(3, harvestReminder.notes());
            ps.setObject(4, harvestReminder.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update harvest reminder", e);
        }
    }

    private static HarvestReminder mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondId = (UUID) rs.getObject("pond_id");
        LocalDate reminderDate = rs.getObject("reminder_date", LocalDate.class);
        Time t = rs.getTime("reminder_time");
        LocalTime reminderTime = t != null ? t.toLocalTime() : null;
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new HarvestReminder(id, pondId, reminderDate, reminderTime, notes, organizationId);
    }
}
