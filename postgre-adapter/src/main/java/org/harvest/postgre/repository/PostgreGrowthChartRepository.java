package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.GrowthChartRepository;
import org.harvest.domain.GrowthChart;
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

public class PostgreGrowthChartRepository implements GrowthChartRepository {

    private static final String REPORT_GROWTH_CHART = "SELECT ponds_id AS pond_id, record_date AS date, average_weight AS average_weight, average_length AS average_length FROM growth_record WHERE organization_id = ? AND (?::uuid IS NULL OR ponds_id = ?) AND record_date >= ? AND record_date <= ? ORDER BY ponds_id, record_date";

    private final DataSource dataSource;

    public PostgreGrowthChartRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreGrowthChartRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<GrowthChart> reportGrowthChart(UUID organizationId, UUID pondsId, LocalDate startDate, LocalDate endDate) {
        List<GrowthChart> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(REPORT_GROWTH_CHART)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, pondsId);
            ps.setObject(3, pondsId);
            ps.setObject(4, startDate);
            ps.setObject(5, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID pondId = (UUID) rs.getObject("pond_id");
                    LocalDate date = rs.getObject("date", LocalDate.class);
                    BigDecimal averageWidth = rs.getBigDecimal("average_weight");
                    BigDecimal averageLength = rs.getBigDecimal("average_length");
                    list.add(new GrowthChart(pondId, date, averageWidth, averageLength));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to report growth chart", e);
        }
        return list;
    }
}
