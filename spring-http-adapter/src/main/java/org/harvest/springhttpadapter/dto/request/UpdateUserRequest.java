package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.harvest.application.dto.value.Role;

@Getter
@Setter
@Schema(description = "Request body untuk update data pengguna")
public class UpdateUserRequest {
    
    @Schema(description = "Nama lengkap pengguna", example = "Siti Rahayu Putri")
    private String name;
    
    @Schema(description = "Role pengguna dalam organisasi", example = "ADMIN", allowableValues = {"ADMIN", "STAFF"})
    private Role role;
}
