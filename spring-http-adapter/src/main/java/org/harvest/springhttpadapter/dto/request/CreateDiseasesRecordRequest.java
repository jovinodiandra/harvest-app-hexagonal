package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk menambah catatan penyakit baru")
public record CreateDiseasesRecordRequest(
        @Schema(description = "ID kolam", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
        UUID pondId,

        @Schema(description = "Nama penyakit", example = "White spot", required = true)
        String diseaseName,

        @Schema(description = "Gejala penyakit", example = "Bintik putih pada tubuh ikan", required = true)
        String symptoms,

        @Schema(description = "Jumlah ikan yang terinfeksi", example = "50", required = true)
        int infectedFishCount,

        @Schema(description = "Tanggal penyakit (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate diseaseDate,

        @Schema(description = "Catatan tambahan", example = "Perlu dilakukan karantina")
        String notes
) {
}
