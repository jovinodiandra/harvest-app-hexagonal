package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.SupplierRepository;
import org.harvest.domain.Supplier;
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

public class PostgreSupplierRepository implements SupplierRepository {

    private static final String SELECT_BY_ID = "SELECT id, name, address, phone, email, notes, organization_id FROM supplier WHERE id = ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, name, address, phone, email, notes, organization_id FROM supplier WHERE organization_id = ? ORDER BY name LIMIT ? OFFSET ?";
    private static final String SELECT_BY_NAME = "SELECT id, name, address, phone, email, notes, organization_id FROM supplier WHERE organization_id = ? AND (name ILIKE ? OR ?::text IS NULL) ORDER BY name LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO supplier (id, name, address, phone, email, notes, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE supplier SET name = ?, address = ?, phone = ?, email = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM supplier WHERE id = ?";

    private final DataSource dataSource;

    public PostgreSupplierRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreSupplierRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(Supplier supplier) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, supplier.id());
            ps.setString(2, supplier.name());
            ps.setString(3, supplier.address());
            ps.setString(4, supplier.phone());
            ps.setString(5, supplier.email());
            ps.setString(6, supplier.notes());
            ps.setObject(7, supplier.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save supplier", e);
        }
    }

    @Override
    public void delete(Supplier supplier) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, supplier.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete supplier", e);
        }
    }

    @Override
    public void update(Supplier supplier) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, supplier.name());
            ps.setString(2, supplier.address());
            ps.setString(3, supplier.phone());
            ps.setString(4, supplier.email());
            ps.setString(5, supplier.notes());
            ps.setObject(6, supplier.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update supplier", e);
        }
    }

    @Override
    public Supplier findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find supplier by id", e);
        }
    }

    @Override
    public List<Supplier> findAllByOrganization(UUID organizationId, Pagination pagination) {
        List<Supplier> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, pagination);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find suppliers by organization", e);
        }
        return list;
    }

    @Override
    public List<Supplier> findByName(UUID organizationId, String name, Pagination pagination) {
        List<Supplier> list = new ArrayList<>();
        String searchPattern = name != null && !name.isBlank() ? "%" + name + "%" : null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_NAME)) {
            ps.setObject(1, organizationId);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setObject(4, pagination.getLimit());
            ps.setObject(5, pagination.offset());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find suppliers by name", e);
        }
        return list;
    }

    private static Supplier mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new Supplier(id, name, address, phone, email, notes, organizationId);
    }
}
