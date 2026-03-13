package org.harvest.postgre.repository;

import org.harvest.application.port.outbound.FishStatisticsRepository;
import org.harvest.domain.FishStatistics;
import org.harvest.postgre.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Report: per-pond stats (total seeds stocked, total alive, total dead) for an organization in a date range.
 * totalPonds = 1 per row (each row is one pond). Aggregates seed, death_record, growth/harvest for the period.
 */
public class PostgreFishStatisticsRepository implements FishStatisticsRepository {

    private static final String REPORT_FISH_STATISTICS =
        "WITH pond_list AS ("
        + "  SELECT p.id AS pond_id FROM ponds p WHERE p.organization_id = ?"
        + "),"
        + " seeds_stocked AS ("
        + "  SELECT s.ponds_id, COALESCE(SUM(s.quantity), 0) AS total_seeds"
        + "  FROM seed s"
        + "  WHERE s.organization_id = ? AND s.stock_date <= ?"
        + "  GROUP BY s.ponds_id"
        + "),"
        + " deaths AS ("
        + "  SELECT d.ponds_id, COALESCE(SUM(d.death_count), 0) AS total_dead"
        + "  FROM death_record d"
        + "  WHERE d.organization_id = ? AND d.record_date >= ? AND d.record_date <= ?"
        + "  GROUP BY d.ponds_id"
        + ")"
        + " SELECT pl.pond_id AS pond_id,"
        + "  1 AS total_ponds,"
        + "  COALESCE(ss.total_seeds, 0)::int AS total_seeds_stocked,"
        + "  (COALESCE(ss.total_seeds, 0) - COALESCE(de.total_dead, 0))::int AS total_fish_alive,"
        + "  COALESCE(de.total_dead, 0)::int AS total_fish_dead"
        + " FROM pond_list pl"
        + " LEFT JOIN seeds_stocked ss ON ss.ponds_id = pl.pond_id"
        + " LEFT JOIN deaths de ON de.ponds_id = pl.pond_id"
        + " WHERE pl.pond_id IN (SELECT id FROM ponds WHERE organization_id = ?)";

    private final DataSource dataSource;

    public PostgreFishStatisticsRepository() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public PostgreFishStatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<FishStatistics> reportFishStatistics(UUID organizationId, LocalDate startDate, LocalDate endDate) {
        List<FishStatistics> list = new ArrayList<>();
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(REPORT_FISH_STATISTICS)) {
            ps.setObject(1, organizationId);
            ps.setObject(2, organizationId);
            ps.setObject(3, endDate);
            ps.setObject(4, organizationId);
            ps.setObject(5, startDate);
            ps.setObject(6, endDate);
            ps.setObject(7, organizationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID pondId = (UUID) rs.getObject("pond_id");
                    int totalPonds = rs.getInt("total_ponds");
                    int totalSeedsStocked = rs.getInt("total_seeds_stocked");
                    int totalFishAlive = rs.getInt("total_fish_alive");
                    int totalFishDead = rs.getInt("total_fish_dead");
                    list.add(new FishStatistics(pondId, totalPonds, totalSeedsStocked, totalFishAlive, totalFishDead));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to report fish statistics", e);
        }
        return list;
    }
}
