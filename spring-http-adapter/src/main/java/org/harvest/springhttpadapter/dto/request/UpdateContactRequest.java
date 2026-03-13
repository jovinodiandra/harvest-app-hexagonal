package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body untuk update data kontak supplier")
public record UpdateContactRequest(
        @Schema(description = "Nama kontak person", example = "Budi Santoso", required = true)
        String name,
        
        @Schema(description = "Alamat kontak", example = "Jl. Mawar No. 5, Jakarta", required = true)
        String address,
        
        @Schema(description = "Nomor telepon kontak", example = "0812-3456-7890", required = true)
        String phone,
        
        @Schema(description = "Email kontak", example = "budi@supplier.com", required = true)
        String email,
        
        @Schema(description = "Catatan tambahan", example = "PIC untuk pemesanan pakan")
        String notes
) {
}
