package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk update catatan pertumbuhan")
public record UpdateGrowthRecordRequest(
        @Schema(description = "UUID kolam yang terkait", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        UUID pondsId,

        @Schema(description = "Tanggal pencatatan (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate recordDate,

        @Schema(description = "Rata-rata panjang ikan (cm)", example = "12.5", required = true)
        BigDecimal averageLength,

        @Schema(description = "Rata-rata berat ikan (gram)", example = "150.0", required = true)
        BigDecimal averageWeight
) {
}
