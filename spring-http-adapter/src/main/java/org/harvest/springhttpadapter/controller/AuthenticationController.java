package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.LoginCommand;
import org.harvest.application.dto.command.RegisterCommand;
import org.harvest.application.dto.result.LoginResult;
import org.harvest.application.dto.result.RegisterResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.springhttpadapter.dto.request.LoginRequest;
import org.harvest.springhttpadapter.dto.request.RegisterRequest;
import org.harvest.springhttpadapter.dto.presenter.LoginPresenter;
import org.harvest.springhttpadapter.dto.presenter.RegisterPresenter;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.LoginResponse;
import org.harvest.springhttpadapter.dto.response.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API untuk autentikasi pengguna (Register & Login)")
public class AuthenticationController {
    private final UseCase<RegisterCommand, RegisterResult> registerUseCase;
    private final UseCase<LoginCommand, LoginResult> loginUseCase;

    public AuthenticationController(UseCase<RegisterCommand, RegisterResult> registerUseCase, UseCase<LoginCommand, LoginResult> loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Operation(
            summary = "Register pengguna baru",
            description = """
                    Mendaftarkan pengguna baru beserta organisasi.
                    
                    **Validasi:**
                    - Email harus unik dan valid
                    - Password minimal 8 karakter
                    - Password dan Confirm Password harus sama
                    - Nama organisasi wajib diisi
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registrasi berhasil",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "userId": "550e8400-e29b-41d4-a716-446655440000",
                                                "organizationId": "660e8400-e29b-41d4-a716-446655440001",
                                                "name": "Budi Santoso",
                                                "email": "budi@ternaklele.com",
                                                "organizationName": "Lele Maju Jaya",
                                                "role": "OWNER",
                                                "createdAt": "2026-03-13T10:30:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validasi gagal",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Error Validasi",
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Password dan Confirm Password tidak sama",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data registrasi pengguna",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Contoh Request Register",
                            value = """
                                    {
                                      "name": "Budi Santoso",
                                      "email": "budi@ternaklele.com",
                                      "password": "Password123!",
                                      "confirmPassword": "Password123!",
                                      "organizationName": "Lele Maju Jaya"
                                    }
                                    """
                    )
            )
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        RegisterCommand registerCommand = new RegisterCommand(registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getConfirmPassword(), registerRequest.getOrganizationName());
        RegisterPresenter registerPresenter = new RegisterPresenter();
        registerUseCase.process(registerCommand, registerPresenter);
        return registerPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Login pengguna",
            description = """
                    Melakukan autentikasi pengguna dan mendapatkan JWT token.
                    
                    **Cara Penggunaan Token:**
                    1. Setelah login berhasil, simpan `accessToken` yang diterima
                    2. Gunakan token pada header `Authorization` untuk request selanjutnya
                    3. Format: `Bearer <accessToken>`
                    
                    **Masa Berlaku Token:** 24 jam
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login berhasil",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Login Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJlbWFpbCI6ImJ1ZGlAdGVybmFrbGVsZS5jb20iLCJyb2xlIjoiT1dORVIiLCJpYXQiOjE3MTAxMzYyMDB9.abc123",
                                                "tokenType": "Bearer",
                                                "user": {
                                                  "userId": "550e8400-e29b-41d4-a716-446655440000",
                                                  "name": "Budi Santoso",
                                                  "email": "budi@ternaklele.com",
                                                  "role": "Owner"
                                                },
                                                "organization": {
                                                  "organizationId": "660e8400-e29b-41d4-a716-446655440001",
                                                  "organizationName": "Lele Maju Jaya"
                                                }
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Email atau password salah",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Error Login",
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Email atau password tidak valid",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Kredensial login",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Contoh Request Login",
                            value = """
                                    {
                                      "email": "vino@gmail.com",
                                      "password": "Qwerty12#"
                                    }
                                    """
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginCommand command = new LoginCommand(loginRequest.getEmail(), loginRequest.getPassword());
        LoginPresenter presenter = new LoginPresenter();
        loginUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
