package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.GrowthRecordRepository;
import org.harvest.domain.GrowthRecord;
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

public class PostgreGrowthRecordRepository implements GrowthRecordRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, record_date, average_length, average_weight, organization_id FROM growth_record WHERE id = ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, ponds_id, record_date, average_length, average_weight, organization_id FROM growth_record WHERE organization_id = ? ORDER BY record_date DESC LIMIT ? OFFSET ?";
    private static final String SELECT_BY_PONDS_AND_ORGANIZATION = "SELECT id, ponds_id, record_date, average_weight, average_length, organization_id FROM growth_record WHERE ponds_id = ? AND organization_id = ? ORDER BY record_date DESC LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO growth_record (id, ponds_id, record_date, average_weight, average_length, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE growth_record SET record_date = ?, average_length = ?, average_weight = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM growth_record WHERE id = ?";

    private final DataSource dataSource;

    public PostgreGrowthRecordRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreGrowthRecordRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(GrowthRecord growthRecords) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, growthRecords.id());
            ps.setObject(2, growthRecords.pondsId());
            ps.setObject(3, growthRecords.recordDate());
            ps.setBigDecimal(4, growthRecords.averageWeight());
            ps.setBigDecimal(5, growthRecords.averageLength());
            ps.setObject(6, growthRecords.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save growth record", e);
        }
    }

    @Override
    public GrowthRecord findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find growth record by id", e);
        }
    }

    @Override
    public void delete(GrowthRecord growthRecords) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, growthRecords.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete growth record", e);
        }
    }

    @Override
    public void update(GrowthRecord growthRecords) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, growthRecords.recordDate());
            ps.setBigDecimal(2, growthRecords.averageLength());
            ps.setBigDecimal(3, growthRecords.averageWeight());
            ps.setObject(4, growthRecords.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update growth record", e);
        }
    }

    @Override
    public List<GrowthRecord> findAllByOrganizationId(UUID organizationId, int offset, int limit) {
        List<GrowthRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find growth records by organization", e);
        }
        return list;
    }

    @Override
    public List<GrowthRecord> findAllByPondsIdAndOrganizationId(UUID pondsId, UUID organizationId, int offset, int limit) {
        List<GrowthRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PONDS_AND_ORGANIZATION)) {
            ps.setObject(1, pondsId);
            ps.setObject(2, organizationId);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find growth records by ponds and organization", e);
        }
        return list;
    }

    private static GrowthRecord mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        LocalDate recordDate = rs.getObject("record_date", LocalDate.class);
        BigDecimal averageWeight = rs.getBigDecimal("average_weight");
        BigDecimal averageLength = rs.getBigDecimal("average_length");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        // GrowthRecord constructor expects (averageLength, averageWeight)
        return new GrowthRecord(id, pondsId, recordDate, averageLength, averageWeight, organizationId);
    }
}
