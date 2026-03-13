package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk menambah catatan kualitas air baru")
public record CreateWatterQualityRequest(
        @Schema(description = "UUID kolam yang terkait", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        UUID pondsId,

        @Schema(description = "Tanggal pencatatan (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate recordDate,

        @Schema(description = "Nilai pH air", example = "7.2", required = true)
        BigDecimal ph,

        @Schema(description = "Suhu air (°C)", example = "28.5", required = true)
        BigDecimal temperature,

        @Schema(description = "Kadar oksigen terlarut (mg/L)", example = "6.5", required = true)
        BigDecimal dissolvedOxygen
) {
}
