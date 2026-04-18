package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.SeedRepository;
import org.harvest.domain.Seed;
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

public class PostgreSeedRepository implements SeedRepository {

    private static final String SELECT_BY_ID = "SELECT id, ponds_id, type, quantity, stock_date, organization_id FROM seed WHERE id = ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, ponds_id, type, quantity, stock_date, organization_id FROM seed WHERE organization_id = ? ORDER BY stock_date DESC LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO seed (id, ponds_id, type, quantity, stock_date, organization_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE seed SET type = ?, quantity = ?, stock_date = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM seed WHERE id = ?";

    private final DataSource dataSource;

    public PostgreSeedRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreSeedRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(Seed seed) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, seed.id());
            ps.setObject(2, seed.pondsId());
            ps.setString(3, seed.type());
            ps.setInt(4, seed.quantity());
            ps.setObject(5, seed.stockDate());
            ps.setObject(6, seed.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save seed", e);
        }
    }

    @Override
    public void delete(Seed seed) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, seed.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete seed", e);
        }
    }

    @Override
    public void Update(Seed seed) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, seed.type());
            ps.setInt(2, seed.quantity());
            ps.setObject(3, seed.stockDate());
            ps.setObject(4, seed.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update seed", e);
        }
    }

    @Override
    public Seed findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find seed by id", e);
        }
    }

    @Override
    public List<Seed> findAllByOrganization(UUID organizationId, Pagination pagination) {
        List<Seed> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setObject(2,pagination);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find seeds by organization", e);
        }
        return list;
    }

    private static Seed mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID pondsId = (UUID) rs.getObject("ponds_id");
        String type = rs.getString("type");
        int quantity = rs.getInt("quantity");
        LocalDate stockDate = rs.getObject("stock_date", LocalDate.class);
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new Seed(id, pondsId, type, quantity, stockDate, organizationId);
    }
}
