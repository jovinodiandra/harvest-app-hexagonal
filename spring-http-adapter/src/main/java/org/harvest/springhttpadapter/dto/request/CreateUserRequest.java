package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.harvest.application.dto.value.Role;

@Getter
@Setter
@Schema(description = "Request body untuk membuat pengguna baru")
public class CreateUserRequest {
    
    @Schema(description = "Nama lengkap pengguna", example = "Dewi Kusuma", required = true)
    private String name;
    
    @Schema(description = "Email pengguna (harus unik)", example = "dewi@ternaklele.com", required = true)
    private String email;
    
    @Schema(description = "Password minimal 8 karakter", example = "SecurePass123!", required = true, minLength = 8)
    private String password;
    
    @Schema(description = "Role pengguna dalam organisasi", example = "STAFF", allowableValues = {"ADMIN", "STAFF"})
    private Role role;
}

