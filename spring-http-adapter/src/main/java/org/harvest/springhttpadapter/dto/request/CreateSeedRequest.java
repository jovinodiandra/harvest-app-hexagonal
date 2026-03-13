package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Request body untuk menambah bibit baru")
public record CreateSeedRequest(
        @Schema(description = "UUID kolam tujuan penebaran bibit", example = "880e8400-e29b-41d4-a716-446655440010", required = true)
        UUID pondsId,
        
        @Schema(description = "Jenis/varietas bibit lele (Sangkuriang, Dumbo, Phyton, Mutiara)", example = "Sangkuriang", required = true)
        String type,
        
        @Schema(description = "Jumlah bibit yang ditebar (dalam ekor)", example = "2000", minimum = "1", required = true)
        int quantity,
        
        @Schema(description = "Tanggal penebaran bibit (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate stockDate
) {
}
