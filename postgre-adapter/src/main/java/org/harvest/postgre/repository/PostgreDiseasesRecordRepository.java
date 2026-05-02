package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.DiseasesRecordRepository;
import org.harvest.domain.DiseasesRecord;
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

public class PostgreDiseasesRecordRepository implements DiseasesRecordRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, disease_name, symptoms, infected_fish_count, diseases_date, notes, organization_id FROM diseases_record WHERE id = ?";
    private static final String SELECT_BY_PONDS = "SELECT id, ponds_id, disease_name, symptoms, infected_fish_count, diseases_date, notes, organization_id FROM diseases_record WHERE ponds_id = ? ORDER BY diseases_date DESC LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO diseases_record (id, ponds_id, disease_name, symptoms, infected_fish_count, diseases_date, notes, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE diseases_record SET ponds_id = ?, disease_name = ?, symptoms = ?, infected_fish_count = ?, diseases_date = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM diseases_record WHERE id = ?";

    private final DataSource dataSource;

    public PostgreDiseasesRecordRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreDiseasesRecordRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(DiseasesRecord diseasesRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, diseasesRecord.id());
            ps.setObject(2, diseasesRecord.pondsId());
            ps.setString(3, diseasesRecord.diseaseName());
            ps.setString(4, diseasesRecord.symptoms());
            ps.setInt(5, diseasesRecord.infectedFishCount());
            ps.setObject(6, diseasesRecord.diseasesDate());
            ps.setString(7, diseasesRecord.notes());
            ps.setObject(8, diseasesRecord.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save diseases record", e);
        }
    }

    @Override
    public void update(DiseasesRecord diseasesRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setObject(1, diseasesRecord.pondsId());
            ps.setString(2, diseasesRecord.diseaseName());
            ps.setString(3, diseasesRecord.symptoms());
            ps.setInt(4, diseasesRecord.infectedFishCount());
            ps.setObject(5, diseasesRecord.diseasesDate());
            ps.setString(6, diseasesRecord.notes());
            ps.setObject(7, diseasesRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update diseases record", e);
        }
    }

    @Override
    public void delete(DiseasesRecord diseasesRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, diseasesRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete diseases record", e);
        }
    }

    @Override
    public DiseasesRecord findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find diseases record by id", e);
        }
    }

    @Override
    public List<DiseasesRecord> findAllByPondsId(UUID pondsId, Pagination pagination) {
        List<DiseasesRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PONDS)) {
            ps.setObject(1, pondsId);
            ps.setObject(2,pagination.getLimit());
            ps.setObject(3,pagination.offset());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find diseases records by ponds", e);
        }
        return list;
    }

    private static DiseasesRecord mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        String diseaseName = rs.getString("disease_name");
        String symptoms = rs.getString("symptoms");
        int infectedFishCount = rs.getInt("infected_fish_count");
        LocalDate diseasesDate = rs.getObject("diseases_date", LocalDate.class);
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new DiseasesRecord(id, pondsId, diseaseName, symptoms, infectedFishCount, diseasesDate, notes, organizationId);
    }
}
