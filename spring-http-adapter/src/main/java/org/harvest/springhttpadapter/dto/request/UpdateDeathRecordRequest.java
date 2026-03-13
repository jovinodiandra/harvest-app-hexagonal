package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk update catatan kematian ikan")
public record UpdateDeathRecordRequest(
        @Schema(description = "UUID kolam", example = "880e8400-e29b-41d4-a716-446655440010", required = true)
        UUID pondsId,

        @Schema(description = "Tanggal pencatatan (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate recordDate,

        @Schema(description = "Jumlah ikan yang mati", example = "5", required = true)
        int deathCount,

        @Schema(description = "Catatan tambahan", example = "Penyakit bakteri")
        String notes
) {
}
