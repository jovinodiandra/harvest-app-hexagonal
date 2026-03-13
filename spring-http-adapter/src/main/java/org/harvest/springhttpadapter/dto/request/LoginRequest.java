package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Request body untuk login pengguna")
public class LoginRequest {
    
    @Schema(hidden = true)
    private UUID id;
    
    @Schema(description = "Email yang terdaftar", example = "budi@ternaklele.com", required = true)
    private String email;
    
    @Schema(description = "Password akun", example = "Password123!", required = true)
    private String password;
}
