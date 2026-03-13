package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.domain.Contact;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostgreContactRepository implements ContactRepository {

    private static final String SELECT_BY_ID = "SELECT id, supplier_id, name, address, phone, email, notes FROM contact WHERE id = ?";
    private static final String SELECT_BY_FILTER = "SELECT id, supplier_id, name, address, phone, email, notes FROM contact WHERE supplier_id = ? AND (?::text IS NULL OR name ILIKE ?) ORDER BY name LIMIT ? OFFSET ?";
    private static final String INSERT = "INSERT INTO contact (id, supplier_id, name, address, phone, email, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE contact SET name = ?, address = ?, phone = ?, email = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM contact WHERE id = ?";

    private final DataSource dataSource;

    public PostgreContactRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreContactRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(Contact contact) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, contact.id());
            ps.setObject(2, contact.supplierId());
            ps.setString(3, contact.name());
            ps.setString(4, contact.address());
            ps.setString(5, contact.phone());
            ps.setString(6, contact.email());
            ps.setString(7, contact.notes());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save contact", e);
        }
    }

    @Override
    public void delete(Contact contact) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, contact.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete contact", e);
        }
    }

    @Override
    public void update(Contact contact) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, contact.name());
            ps.setString(2, contact.address());
            ps.setString(3, contact.phone());
            ps.setString(4, contact.email());
            ps.setString(5, contact.notes());
            ps.setObject(6, contact.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update contact", e);
        }
    }

    @Override
    public Contact findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find contact by id", e);
        }
    }

    @Override
    public List<Contact> findByFilter(UUID supplierId, String name, int offset, int limit) {
        List<Contact> list = new ArrayList<>();
        String searchPattern = name != null && !name.isBlank() ? "%" + name + "%" : null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_FILTER)) {
            ps.setObject(1, supplierId);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setInt(4, limit);
            ps.setInt(5, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find contacts by filter", e);
        }
        return list;
    }

    private static Contact mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        UUID supplierId = (UUID) rs.getObject("supplier_id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String notes = rs.getString("notes");
        return new Contact(id, supplierId, name, address, phone, email, notes);
    }
}
