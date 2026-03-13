package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.HarvestRecordRepository;
import org.harvest.domain.HarvestRecord;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreHarvestRecordRepository implements HarvestRecordRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes FROM harvest_record WHERE id = ?";
    private static final String SELECT_BY_PONDS = "SELECT id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes FROM harvest_record WHERE ponds_id = ? ORDER BY harvest_date DESC LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ORGANIZATION = "SELECT id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes FROM harvest_record WHERE organization_id = ? ORDER BY harvest_date DESC LIMIT ? OFFSET ?";
    private static final String SELECT_REPORT = "SELECT id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes FROM harvest_record WHERE organization_id = ? AND (?::uuid IS NULL OR ponds_id = ?) AND (?::date IS NULL OR harvest_date >= ?) AND (?::date IS NULL OR harvest_date <= ?) ORDER BY harvest_date DESC";
    private static final String INSERT = "INSERT INTO harvest_record (id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE harvest_record SET ponds_id = ?, ponds_name = ?, harvest_date = ?, harvest_fish_count = ?, total_weight = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM harvest_record WHERE id = ?";

    private final DataSource dataSource;

    public PostgreHarvestRecordRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreHarvestRecordRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(HarvestRecord harvestRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, harvestRecord.id());
            ps.setObject(2, harvestRecord.pondsId());
            ps.setString(3, harvestRecord.pondsName());
            ps.setObject(4, harvestRecord.harvestDate());
            ps.setObject(5, harvestRecord.organizationId());
            ps.setInt(6, harvestRecord.harvestFishCount());
            ps.setBigDecimal(7, harvestRecord.totalWeight());
            ps.setString(8, harvestRecord.notes());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save harvest record", e);
        }
    }

    @Override
    public HarvestRecord findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find harvest record by id", e);
        }
    }

    @Override
    public void delete(HarvestRecord harvestRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, harvestRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete harvest record", e);
        }
    }

    @Override
    public void update(HarvestRecord harvestRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, harvestRecord.pondsId());
            ps.setString(2, harvestRecord.pondsName());
            ps.setObject(3, harvestRecord.harvestDate());
            ps.setInt(4, harvestRecord.harvestFishCount());
            ps.setBigDecimal(5, harvestRecord.totalWeight());
            ps.setString(6, harvestRecord.notes());
            ps.setObject(7, harvestRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update harvest record", e);
        }
    }

    @Override
    public List<HarvestRecord> findAllByPondsId(UUID pondsId, int offset, int limit) {
        List<HarvestRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PONDS)) {
            ps.setObject(1, pondsId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find harvest records by ponds", e);
        }
        return list;
    }

    @Override
    public List<HarvestRecord> findAllByOrganizationId(UUID organizationId, int offset, int limit) {
        List<HarvestRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find harvest records by organization", e);
        }
        return list;
    }

    @Override
    public List<HarvestRecord> findReportByOrganizationId(UUID organizationId, UUID pondId, LocalDate startDate, LocalDate endDate) {
        List<HarvestRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_REPORT)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, pondId);
            ps.setObject(3, pondId);
            ps.setObject(4, startDate);
            ps.setObject(5, startDate);
            ps.setObject(6, endDate);
            ps.setObject(7, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find harvest report", e);
        }
        return list;
    }

    private static HarvestRecord mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        String pondsName = rs.getString("ponds_name");
        LocalDate harvestDate = rs.getObject("harvest_date", LocalDate.class);
        UUID organizationId = (UUID) rs.getObject("organization_id");
        int harvestFishCount = rs.getInt("harvest_fish_count");
        BigDecimal totalWeight = rs.getBigDecimal("total_weight");
        String notes = rs.getString("notes");
        return new HarvestRecord(id, pondsName, pondsId, harvestDate, organizationId, harvestFishCount, totalWeight, notes);
    }
}
