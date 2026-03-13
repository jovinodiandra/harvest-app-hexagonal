package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Request body untuk registrasi pengguna baru")
public class RegisterRequest {
    
    @Schema(description = "Nama lengkap pengguna", example = "Budi Santoso", required = true)
    private String name;
    
    @Schema(description = "Nama organisasi/usaha budidaya", example = "Lele Maju Jaya", required = true)
    private String organizationName;
    
    @Schema(description = "Konfirmasi password (harus sama dengan password)", example = "Password123!", required = true)
    private String confirmPassword;
    
    @Schema(description = "Email pengguna (akan digunakan untuk login)", example = "budi@ternaklele.com", required = true)
    private String email;
    
    @Schema(description = "Password minimal 8 karakter", example = "Password123!", required = true, minLength = 8)
    private String password;
}
