package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.DeathRecordRepository;
import org.harvest.domain.DeathRecord;
import org.harvest.postgre.config.DatabaseConfig;
import org.harvest.shared.query.Pagination;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreDeathRecordRepository implements DeathRecordRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, record_date, death_count, notes, organization_id FROM death_record WHERE id = ?";
    private static final String SELECT_BY_PONDS = "SELECT id, ponds_id, record_date, death_count, notes, organization_id FROM death_record WHERE ponds_id = ? ORDER BY record_date DESC LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ORGANIZATION = "SELECT id, ponds_id, record_date, death_count, notes, organization_id FROM death_record WHERE organization_id = ? ORDER BY record_date DESC";
    private static final String INSERT = "INSERT INTO death_record (id, ponds_id, record_date, death_count, notes, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE death_record SET record_date = ?, death_count = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM death_record WHERE id = ?";

    private final DataSource dataSource;

    public PostgreDeathRecordRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreDeathRecordRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(DeathRecord deathRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, deathRecord.id());
            ps.setObject(2, deathRecord.pondsId());
            ps.setObject(3, deathRecord.recordDate());
            ps.setInt(4, deathRecord.deathCount());
            ps.setString(5, deathRecord.notes());
            ps.setObject(6, deathRecord.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save death record", e);
        }
    }

    @Override
    public DeathRecord findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find death record by id", e);
        }
    }

    @Override
    public void delete(DeathRecord deathRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, deathRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete death record", e);
        }
    }

    @Override
    public void update(DeathRecord deathRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, deathRecord.recordDate());
            ps.setInt(2, deathRecord.deathCount());
            ps.setString(3, deathRecord.notes());
            ps.setObject(4, deathRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update death record", e);
        }
    }



    @Override
    public List<DeathRecord> findAllByPondsId(UUID pondsId, Pagination pagination) {
        List<DeathRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PONDS)) {
            ps.setObject(1, pondsId);
            ps.setObject(2, pagination);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find death records by ponds", e);
        }
        return list;
    }

    @Override
    public List<DeathRecord> findAllByOrganizationId(UUID organizationId) {
        List<DeathRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find death records by organization", e);
        }
        return list;
    }

    private static DeathRecord mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        LocalDate recordDate = rs.getObject("record_date", LocalDate.class);
        int deathCount = rs.getInt("death_count");
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new DeathRecord(id, pondsId, recordDate, deathCount, notes, organizationId);
    }
}
