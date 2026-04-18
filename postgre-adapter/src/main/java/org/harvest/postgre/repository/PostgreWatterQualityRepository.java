package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.WatterQualityRepository;
import org.harvest.domain.WatterQuality;
import org.harvest.postgre.config.DatabaseConfig;
import org.harvest.shared.query.Pagination;

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

public class PostgreWatterQualityRepository implements WatterQualityRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, record_date, ph, temperature, dissolved_oxygen, organization_id FROM watter_quality WHERE id = ?";
    private static final String SELECT_BY_PONDS = "SELECT id, ponds_id, record_date, ph, temperature, dissolved_oxygen, organization_id FROM watter_quality WHERE ponds_id = ? ORDER BY record_date DESC LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO watter_quality (id, ponds_id, record_date, ph, temperature, dissolved_oxygen, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE watter_quality SET record_date = ?, ph = ?, temperature = ?, dissolved_oxygen = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM watter_quality WHERE id = ?";

    private final DataSource dataSource;

    public PostgreWatterQualityRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreWatterQualityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(WatterQuality watterQuality) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, watterQuality.id());
            ps.setObject(2, watterQuality.pondsID());
            ps.setObject(3, watterQuality.recordDate());
            ps.setBigDecimal(4, watterQuality.ph());
            ps.setBigDecimal(5, watterQuality.temperature());
            ps.setBigDecimal(6, watterQuality.dissolvedOxygen());
            ps.setObject(7, watterQuality.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save watter quality", e);
        }
    }

    @Override
    public WatterQuality findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find watter quality by id", e);
        }
    }

    @Override
    public void delete(WatterQuality watterQuality) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, watterQuality.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete watter quality", e);
        }
    }

    @Override
    public void update(WatterQuality watterQuality) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, watterQuality.recordDate());
            ps.setBigDecimal(2, watterQuality.ph());
            ps.setBigDecimal(3, watterQuality.temperature());
            ps.setBigDecimal(4, watterQuality.dissolvedOxygen());
            ps.setObject(5, watterQuality.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update watter quality", e);
        }
    }

    @Override
    public List<WatterQuality> findAllByPondsId(UUID pondsId, Pagination pagination) {
        List<WatterQuality> list = new ArrayList<>();
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
            throw new RuntimeException("Failed to find watter quality by ponds", e);
        }
        return list;
    }

    private static WatterQuality mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        LocalDate recordDate = rs.getObject("record_date", LocalDate.class);
        BigDecimal ph = rs.getBigDecimal("ph");
        BigDecimal temperature = rs.getBigDecimal("temperature");
        BigDecimal dissolvedOxygen = rs.getBigDecimal("dissolved_oxygen");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new WatterQuality(id, pondsId, recordDate, ph, temperature, dissolvedOxygen, organizationId);
    }
}
