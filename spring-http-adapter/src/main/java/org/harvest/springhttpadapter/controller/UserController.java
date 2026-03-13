package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateUserCommand;
import org.harvest.application.dto.command.DeleteUserCommand;
import org.harvest.application.dto.command.UpdateUserCommand;
import org.harvest.application.dto.query.ViewUserQuery;
import org.harvest.application.dto.result.CreateUserResult;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdateUserResult;
import org.harvest.application.dto.result.ViewUserResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreateUserPresenter;
import org.harvest.springhttpadapter.dto.presenter.DeleteUserPresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdateUserPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewUserPresenter;
import org.harvest.springhttpadapter.dto.request.CreateUserRequest;
import org.harvest.springhttpadapter.dto.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
@RestController
@Tag(name = "Users", description = "API untuk manajemen pengguna dalam organisasi")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UseCase<ViewUserQuery, ViewUserResult> viewUserUseCase;
    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateUserCommand, CreateUserResult> createUserUseCase;
    private final UseCase<UpdateUserCommand, UpdateUserResult> updateUserUseCase;
    private final UseCase<DeleteUserCommand, DefaultResult> deleteUserUseCase;

    public UserController(UseCase<ViewUserQuery, ViewUserResult> viewUserUseCase, SessionExtractor sessionExtractor, UseCase<CreateUserCommand, CreateUserResult> createUserUseCase, UseCase<UpdateUserCommand, UpdateUserResult> updateUserUseCase, UseCase<DeleteUserCommand, DefaultResult> deleteUserUseCase) {
        this.viewUserUseCase = viewUserUseCase;
        this.sessionExtractor = sessionExtractor;
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @Operation(
            summary = "Lihat daftar pengguna",
            description = """
                    Mengambil daftar semua pengguna dalam organisasi yang sama dengan pengguna yang login.
                    
                    **Fitur:**
                    - Mendukung pagination
                    - Hanya menampilkan user dalam organisasi yang sama
                    - Diurutkan berdasarkan tanggal pembuatan (terbaru lebih dulu)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Berhasil mengambil daftar pengguna",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Daftar User",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "users": [
                                                  {
                                                    "id": "550e8400-e29b-41d4-a716-446655440000",
                                                    "name": "Budi Santoso",
                                                    "email": "budi@ternaklele.com",
                                                    "role": "Owner"
                                                  },
                                                  {
                                                    "id": "550e8400-e29b-41d4-a716-446655440001",
                                                    "name": "Siti Rahayu",
                                                    "email": "siti@ternaklele.com",
                                                    "role": "Admin"
                                                  },
                                                  {
                                                    "id": "550e8400-e29b-41d4-a716-446655440002",
                                                    "name": "Ahmad Wijaya",
                                                    "email": "ahmad@ternaklele.com",
                                                    "role": "Staff"
                                                  }
                                                ]
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Tidak terautentikasi",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Token tidak valid atau sudah kadaluarsa",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping()
    public ResponseEntity<?> viewUser(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false) Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false) Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewUserQuery viewUserQuery = new ViewUserQuery(userSession, page, limit);
        ViewUserPresenter viewUserPresenter = new ViewUserPresenter();
        viewUserUseCase.process(viewUserQuery, viewUserPresenter);
        return viewUserPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Tambah pengguna baru",
            description = """
                    Membuat pengguna baru dalam organisasi.
                    
                    **Ketentuan:**
                    - Hanya Owner atau Admin yang dapat menambah pengguna
                    - Email harus unik dalam sistem
                    - Password minimal 8 karakter
                    - Pengguna baru otomatis tergabung dalam organisasi yang sama
                    
                    **Role yang tersedia:**
                    - `ADMIN` - Administrator dengan akses penuh
                    - `STAFF` - Staf operasional
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pengguna berhasil dibuat",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "770e8400-e29b-41d4-a716-446655440003",
                                                "name": "Dewi Kusuma",
                                                "email": "dewi@ternaklele.com",
                                                "role": "STAFF",
                                                "createdAt": "2026-03-13T14:30:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Data tidak valid",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Email sudah terdaftar dalam sistem",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Tidak memiliki akses",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Anda tidak memiliki izin untuk menambah pengguna",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data pengguna baru",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Contoh Request Tambah User",
                            value = """
                                    {
                                      "name": "Dewi Kusuma",
                                      "email": "dewi@ternaklele.com",
                                      "password": "SecurePass123!"
                                    }
                                    """
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<?> createUser(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CreateUserRequest createUserRequest) {

        UserSession userSession = sessionExtractor.extract(token);
        CreateUserCommand command = new CreateUserCommand(userSession, createUserRequest.getName(), createUserRequest.getEmail(), createUserRequest.getPassword());
        CreateUserPresenter createUserPresenter = new CreateUserPresenter();
        createUserUseCase.process(command, createUserPresenter);
        return createUserPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Update data pengguna",
            description = """
                    Mengupdate informasi pengguna.
                    
                    **Ketentuan:**
                    - Owner dapat mengupdate semua pengguna
                    - Admin dapat mengupdate Staff
                    - User dapat mengupdate data diri sendiri (kecuali role)
                    
                    **Field yang dapat diupdate:**
                    - `name` - Nama pengguna
                    - `role` - Role pengguna (hanya oleh Owner/Admin)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pengguna berhasil diupdate",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "550e8400-e29b-41d4-a716-446655440001",
                                                "name": "Siti Rahayu Putri",
                                                "email": "siti@ternaklele.com",
                                                "role": "ADMIN",
                                                "updatedAt": "2026-03-13T15:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pengguna tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Pengguna dengan ID tersebut tidak ditemukan",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data update pengguna",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Contoh Request Update User",
                            value = """
                                    {
                                      "name": "Siti Rahayu Putri",
                                      "role": "ADMIN"
                                    }
                                    """
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String token,
            @Parameter(description = "UUID pengguna yang akan diupdate", example = "550e8400-e29b-41d4-a716-446655440001") @PathVariable UUID id,
            @RequestBody UpdateUserRequest updateUserRequest) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateUserCommand command = new UpdateUserCommand(userSession, id, updateUserRequest.getName(), updateUserRequest.getRole());
        UpdateUserPresenter updateUserPresenter = new UpdateUserPresenter();
        updateUserUseCase.process(command, updateUserPresenter);
        return updateUserPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus pengguna",
            description = """
                    Menghapus pengguna dari sistem.
                    
                    **Ketentuan:**
                    - Hanya Owner yang dapat menghapus pengguna
                    - Owner tidak dapat menghapus diri sendiri
                    - Data yang terkait dengan pengguna akan tetap tersimpan
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pengguna berhasil dihapus",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Pengguna berhasil dihapus",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Tidak memiliki akses",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Hanya Owner yang dapat menghapus pengguna",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pengguna tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Pengguna dengan ID tersebut tidak ditemukan",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID pengguna yang akan dihapus", example = "550e8400-e29b-41d4-a716-446655440002") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteUserCommand command = new DeleteUserCommand(userSession, id);
        DeleteUserPresenter deleteUserPresenter = new DeleteUserPresenter();
        deleteUserUseCase.process(command, deleteUserPresenter);
        return deleteUserPresenter.getResponseEntity();
    }
}

