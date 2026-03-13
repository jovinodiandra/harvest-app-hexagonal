package org.harvest.postgre.repository;

import org.harvest.application.dto.value.Role;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.domain.User;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreUserRepository implements UserRepository {

    private static final String SELECT_EXISTS_BY_EMAIL = "SELECT 1 FROM harvest_user WHERE email = ? LIMIT 1";
    private static final String SELECT_BY_EMAIL = "SELECT id, name, email, password, organization_id, role FROM harvest_user WHERE email = ?";
    private static final String SELECT_BY_EMAIL_AND_PASSWORD = "SELECT id, name, email, password, organization_id, role FROM harvest_user WHERE email = ? AND password = ?";
    private static final String SELECT_BY_ID = "SELECT id, name, email, password, organization_id, role FROM harvest_user WHERE id = ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, name, email, password, organization_id, role FROM harvest_user WHERE organization_id = ? ORDER BY name LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO harvest_user (id, name, email, password, organization_id, role) VALUES (?, ?, ?, ?, ?, ?::harvest_role)";
    private static final String UPDATE = "UPDATE harvest_user SET name = ?, email = ?, password = ?, role = ?::harvest_role WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM harvest_user WHERE id = ?";

    private final DataSource dataSource;

    public PostgreUserRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_EXISTS_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check user existence by email", e);
        }
    }

    @Override
    public List<User> findAllByOrganizationId(UUID organizationId, int offset, int limit) {
        List<User> list = new ArrayList<>();
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
            throw new RuntimeException("Failed to find users by organization", e);
        }
        return list;
    }

    @Override
    public User findByEmail(String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
    }

    @Override
    public void save(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, user.id());
            ps.setString(2, user.name());
            ps.setString(3, user.email());
            ps.setString(4, user.password());
            ps.setObject(5, user.organizationId());
            ps.setString(6, user.role().getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void delete(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, user.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public User findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id", e);
        }
    }

    @Override
    public User updateUser(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setString(3, user.password());
            ps.setString(4, user.role().getDescription());
            ps.setObject(5, user.id());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL_AND_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email and password", e);
        }
    }

    private static User mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        String roleStr = rs.getString("role");
        Role role = Role.valueOf(roleStr != null ? roleStr.toUpperCase() : "USER");
        return new User(id, name, email, password, organizationId, role);
    }
}
