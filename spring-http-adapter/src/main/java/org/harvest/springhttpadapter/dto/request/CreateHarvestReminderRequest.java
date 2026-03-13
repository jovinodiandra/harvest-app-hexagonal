package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Request body untuk menambah pengingat panen")
public record CreateHarvestReminderRequest(
        @Schema(description = "UUID kolam", required = true)
        UUID pondId,

        @Schema(description = "Tanggal pengingat (format: YYYY-MM-DD)", example = "2026-03-15", required = true)
        LocalDate reminderDate,

        @Schema(description = "Waktu pengingat (format: HH:mm)", example = "07:00", required = true)
        LocalTime reminderTime,

        @Schema(description = "Catatan tambahan", example = "Panen pagi, estimasi 50kg")
        String notes
) {
}
