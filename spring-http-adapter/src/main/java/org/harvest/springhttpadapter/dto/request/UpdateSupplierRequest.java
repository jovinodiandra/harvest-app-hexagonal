package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body untuk update data supplier")
public record UpdateSupplierRequest(
        @Schema(description = "Nama supplier", example = "PT Pakan Lele Sejahtera", required = true)
        String name,
        
        @Schema(description = "Alamat supplier", example = "Jl. Raya Industri No. 123, Bekasi", required = true)
        String address,
        
        @Schema(description = "Nomor telepon supplier", example = "021-88889999", required = true)
        String phone,
        
        @Schema(description = "Email supplier", example = "sales@pakanlele.co.id", required = true)
        String email,
        
        @Schema(description = "Catatan tambahan tentang supplier", example = "Supplier utama pakan berkualitas tinggi")
        String notes
) {
}
