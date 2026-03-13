package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk update data bibit")
public record UpdateSeedRequest(
        @Schema(description = "UUID bibit yang akan diupdate", example = "990e8400-e29b-41d4-a716-446655440020", required = true)
        UUID id,
        
        @Schema(description = "UUID kolam (bisa diubah untuk memindahkan bibit)", example = "880e8400-e29b-41d4-a716-446655440010")
        UUID pondId,
        
        @Schema(description = "Jenis/varietas bibit lele", example = "Sangkuriang Premium")
        String type,
        
        @Schema(description = "Jumlah bibit saat ini (dalam ekor)", example = "1850", minimum = "0")
        int quantity,
        
        @Schema(description = "Tanggal penebaran bibit (format: YYYY-MM-DD)", example = "2026-03-13")
        LocalDate stockDate
) {
}
