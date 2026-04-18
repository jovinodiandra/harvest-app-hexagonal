package org.harvest.postgre.repository;

import org.harvest.application.dto.value.TransactionType;
import org.harvest.application.port.outbound.FinanceRecordRepository;
import org.harvest.domain.FinanceRecord;
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

public class PostgreFinanceRecordRepository implements FinanceRecordRepository {

    private static final String SELECT_BY_ID = "SELECT id, transaction_type, transaction_date, amount, category, notes, organization_id FROM finance_record WHERE id = ?";
    private static final String SELECT_BY_FILTERS = "SELECT id, transaction_type, transaction_date, amount, category, notes, organization_id\n" +
            "FROM finance_record\n" +
            "WHERE organization_id = ?::uuid\n" +
            "AND (?::text IS NULL OR transaction_type = ?::transaction_type_enum)\n" +
            "AND (?::date IS NULL OR transaction_date >= ?::date)\n" +
            "AND (?::date IS NULL OR transaction_date <= ?::date)\n" +
            "ORDER BY transaction_date DESC\n" +
            "LIMIT ? OFFSET ?";
    private static final String SELECT_ALL_BY_ORGANIZATION = "SELECT id, transaction_type, transaction_date, amount, category, notes, organization_id FROM finance_record WHERE organization_id = ? AND (?::date IS NULL OR transaction_date >= ?) AND (?::date IS NULL OR transaction_date <= ?) ORDER BY transaction_date DESC";
    private static final String INSERT = "INSERT INTO finance_record (id, transaction_type, transaction_date, amount, category, notes, organization_id) VALUES (?, ?::transaction_type_enum, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE finance_record SET transaction_type = ?::transaction_type_enum, transaction_date = ?, amount = ?, category = ?, notes = ? WHERE id = ?::uuid";
    private static final String DELETE = "DELETE FROM finance_record WHERE id = ?";

    private final DataSource dataSource;

    public PostgreFinanceRecordRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreFinanceRecordRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UUID nextId() {
        return UUID.randomUUID();
    }

    @Override
    public void save(FinanceRecord financeRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setObject(1, financeRecord.id());
            ps.setString(2, financeRecord.transactionType().getDescription());
            ps.setObject(3, financeRecord.transactionDate());
            ps.setBigDecimal(4, financeRecord.amount());
            ps.setString(5, financeRecord.category());
            ps.setString(6, financeRecord.notes());
            ps.setObject(7, financeRecord.organizationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save finance record", e);
        }
    }

    @Override
    public void delete(FinanceRecord financeRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setObject(1, financeRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete finance record", e);
        }
    }

    @Override
    public void update(FinanceRecord financeRecord) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, financeRecord.transactionType().getDescription());
            ps.setObject(2, financeRecord.transactionDate());
            ps.setBigDecimal(3, financeRecord.amount());
            ps.setString(4, financeRecord.category());
            ps.setString(5, financeRecord.notes());
            ps.setObject(6, financeRecord.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update finance record", e);
        }
    }

    @Override
    public FinanceRecord findById(UUID id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find finance record by id", e);
        }
    }

    @Override
    public List<FinanceRecord> findByFilters(UUID organizationId, TransactionType transactionType, LocalDate startDate, LocalDate endDate, Pagination pagination) {
        List<FinanceRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_FILTERS)) {
            ps.setObject(1, organizationId);
            ps.setString(2, transactionType != null ? transactionType.getDescription() : null);
            ps.setString(3, transactionType != null ? transactionType.getDescription() : null);
            ps.setObject(4, startDate);
            ps.setObject(5, startDate);
            ps.setObject(6, endDate);
            ps.setObject(7, endDate);
          ps.setObject(8, pagination);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find finance records by filters", e);
        }
        return list;
    }

    @Override
    public List<FinanceRecord> findAllByOrganizationId(UUID organizationId, LocalDate startDate, LocalDate endDate) {
        List<FinanceRecord> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_ORGANIZATION)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, startDate);
            ps.setObject(3, startDate);
            ps.setObject(4, endDate);
            ps.setObject(5, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find finance records by organization", e);
        }
        return list;
    }

    private static FinanceRecord mapRow(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String tt = rs.getString("transaction_type");
        TransactionType transactionType = tt != null && tt.equalsIgnoreCase("expense") ? TransactionType.EXPENSE : TransactionType.INCOME;
        LocalDate transactionDate = rs.getObject("transaction_date", LocalDate.class);
        BigDecimal amount = rs.getBigDecimal("amount");
        String category = rs.getString("category");
        String notes = rs.getString("notes");
        UUID organizationId = (UUID) rs.getObject("organization_id");
        return new FinanceRecord(id, transactionType, transactionDate, amount, category, notes, organizationId);
    }
}
