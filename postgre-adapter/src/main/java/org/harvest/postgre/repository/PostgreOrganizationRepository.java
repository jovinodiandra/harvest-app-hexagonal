package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.OrganizationRepository;
import org.harvest.domain.Organization;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgreOrganizationRepository implements OrganizationRepository {

    private static final String SELECT_BY_ID = "SELECT id, name FROM organization WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT id, name FROM organization WHERE name = ?";
    private static final String INSERT = "INSERT INTO organization (id, name) VALUES (?, ?)";

    private final DataSource dataSource;

    public PostgreOrganizationRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreOrganizationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(Organization organization) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, organization.id());
            ps.setString(2, organization.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save organization", e);
        }
    }

    @Override
    public Organization findByName(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find organization by name", e);
        }
    }

    @Override
    public Organization findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find organization by id", e);
        }
    }

    private static Organization mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        return new Organization(id, name);
    }
}
