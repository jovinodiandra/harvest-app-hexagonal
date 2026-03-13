package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Request body untuk menambah jadwal pemberian pakan")
public record CreateFeedScheduleRequest(
        @Schema(description = "UUID kolam", required = true)
        UUID pondsId,
        
        @Schema(description = "Jenis pakan (pellet, cacing, dll)", example = "Pellet Hi-Pro", required = true)
        String feedType,
        
        @Schema(description = "Jumlah pakan dalam kg", example = "2.5", required = true)
        BigDecimal feedAmount,
        
        @Schema(description = "Waktu pemberian pakan (format: HH:mm)", example = "07:00", required = true)
        LocalTime feedTime
) {
}
