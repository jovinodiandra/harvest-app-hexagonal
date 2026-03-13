package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body untuk update data kolam")
public record UpdatePondsRequest(
        @Schema(description = "Nama identifikasi kolam", example = "Kolam Premium A1")
        String name,
        
        @Schema(description = "Lokasi fisik kolam dalam area budidaya", example = "Area Utara - Blok 1 (Renovasi)")
        String location,
        
        @Schema(description = "Kapasitas maksimal bibit ikan (dalam ekor)", example = "6000", minimum = "100")
        int capacity
) {
}
