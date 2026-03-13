package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.query.ViewFishStatisticQuery;
import org.harvest.application.dto.query.ViewGrowthChartQuery;
import org.harvest.application.dto.result.ViewFishStatisticResult;
import org.harvest.application.dto.result.ViewGrowthChartResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.ViewFishStatisticsPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewGrowthChartPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/reports")
@RestController
@Tag(name = "Reports", description = "API untuk laporan statistik ikan dan grafik pertumbuhan")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<ViewFishStatisticQuery, ViewFishStatisticResult> viewFishStatisticUseCase;
    private final UseCase<ViewGrowthChartQuery, ViewGrowthChartResult> viewGrowthChartUseCase;

    public ReportController(SessionExtractor sessionExtractor,
                            UseCase<ViewFishStatisticQuery, ViewFishStatisticResult> viewFishStatisticUseCase,
                            UseCase<ViewGrowthChartQuery, ViewGrowthChartResult> viewGrowthChartUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.viewFishStatisticUseCase = viewFishStatisticUseCase;
        this.viewGrowthChartUseCase = viewGrowthChartUseCase;
    }

    @Operation(
            summary = "Laporan statistik ikan",
            description = "Mengambil laporan statistik ikan per kolam dalam rentang tanggal tertentu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil laporan statistik ikan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping("/fish-statistics")
    public ResponseEntity<?> viewFishStatistics(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Tanggal awal (format: YYYY-MM-DD)", required = true) @RequestParam LocalDate startDate,
            @Parameter(description = "Tanggal akhir (format: YYYY-MM-DD)", required = true) @RequestParam LocalDate endDate) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFishStatisticQuery query = new ViewFishStatisticQuery(userSession, startDate, endDate);
        ViewFishStatisticsPresenter presenter = new ViewFishStatisticsPresenter();
        viewFishStatisticUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Laporan grafik pertumbuhan",
            description = "Mengambil data grafik pertumbuhan ikan untuk kolam tertentu dalam rentang tanggal."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil data grafik pertumbuhan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping("/growth-chart")
    public ResponseEntity<?> viewGrowthChart(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam", required = true) @RequestParam UUID pondsId,
            @Parameter(description = "Tanggal awal (format: YYYY-MM-DD)", required = true) @RequestParam LocalDate startDate,
            @Parameter(description = "Tanggal akhir (format: YYYY-MM-DD)", required = true) @RequestParam LocalDate endDate) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewGrowthChartQuery query = new ViewGrowthChartQuery(userSession, pondsId, startDate, endDate);
        ViewGrowthChartPresenter presenter = new ViewGrowthChartPresenter();
        viewGrowthChartUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }
}
