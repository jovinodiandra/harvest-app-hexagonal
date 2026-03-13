package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk menambah catatan panen baru")
public record CreateHarvestRecordRequest(
        @Schema(description = "UUID kolam", example = "880e8400-e29b-41d4-a716-446655440010", required = true)
        UUID pondId,

        @Schema(description = "Tanggal panen (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate harvestDate,

        @Schema(description = "Jumlah ikan yang dipanen", example = "100", required = true)
        int harvestFishCount,

        @Schema(description = "Total berat panen (kg)", example = "150.5", required = true)
        BigDecimal totalWeight,

        @Schema(description = "Catatan tambahan", example = "Panen kolam A")
        String notes
) {
}
