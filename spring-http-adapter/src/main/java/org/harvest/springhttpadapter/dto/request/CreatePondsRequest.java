package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body untuk membuat kolam baru")
public record CreatePondsRequest(
        @Schema(description = "Nama identifikasi kolam", example = "Kolam A1", required = true)
        String name,
        
        @Schema(description = "Lokasi fisik kolam dalam area budidaya", example = "Area Utara - Blok 1", required = true)
        String location,
        
        @Schema(description = "Kapasitas maksimal bibit ikan (dalam ekor)", example = "5000", minimum = "100", required = true)
        int capacity
) {
}
