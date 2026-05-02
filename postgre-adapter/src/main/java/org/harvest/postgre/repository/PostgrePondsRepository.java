package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.domain.Ponds;
import org.harvest.postgre.config.DatabaseConfig;
import org.harvest.shared.query.Pagination;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgrePondsRepository implements PondsRepository {

    private static final String SELECT_BY_ID = "SELECT id, name, capacity, location, organization_id FROM ponds WHERE id = ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, name, capacity, location, organization_id FROM ponds WHERE organization_id = ? ORDER BY name LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO ponds (id, name, capacity, location, organization_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE ponds SET name = ?, capacity = ?, location = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM ponds WHERE id = ?";

    private final DataSource dataSource;

    public PostgrePondsRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgrePondsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(Ponds ponds) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, ponds.id());
            ps.setString(2, ponds.name());
            ps.setInt(3, ponds.capacity());
            ps.setString(4, ponds.location());
            ps.setObject(5, ponds.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save ponds", e);
        }
    }

    @Override
    public Ponds findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ponds by id", e);
        }
    }

    @Override
    public List<Ponds> findAllByOrganizationId(UUID organizationId, Pagination pagination) {
        List<Ponds> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
           ps.setObject(2, pagination.getLimit());
           ps.setObject(3, pagination.offset());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ponds by organization", e);
        }
        return list;
    }

    @Override
    public void delete(Ponds ponds) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, ponds.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete ponds", e);
        }
    }

    @Override
    public void update(Ponds ponds) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, ponds.name());
            ps.setInt(2, ponds.capacity());
            ps.setString(3, ponds.location());
            ps.setObject(4, ponds.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update ponds", e);
        }
    }

    private static Ponds mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        int capacity = rs.getInt("capacity");
        String location = rs.getString("location");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new Ponds(id, name, capacity, location, organizationId);
    }
}
