package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreatePondsCommand;
import org.harvest.application.dto.command.DeletePondsCommand;
import org.harvest.application.dto.command.UpdatePondsCommand;
import org.harvest.application.dto.query.ViewPondsQuery;
import org.harvest.application.dto.result.CreatePondsResult;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdatePondsResult;
import org.harvest.application.dto.result.ViewPondsResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreatePondsPresenter;
import org.harvest.springhttpadapter.dto.presenter.DeletePondsPresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdatePondsPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewPondsPresenter;
import org.harvest.springhttpadapter.dto.request.CreatePondsRequest;
import org.harvest.springhttpadapter.dto.request.UpdatePondsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/ponds")
@RestController
@Tag(name = "Ponds", description = "API untuk manajemen kolam budidaya ikan lele")
@SecurityRequirement(name = "bearerAuth")
public class PondController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreatePondsCommand, CreatePondsResult> createPondsUseCase;
    private final UseCase<ViewPondsQuery, ViewPondsResult> viewPondsUseCase;
    private final UseCase<UpdatePondsCommand, UpdatePondsResult> updatePondsUseCase;
    private final UseCase<DeletePondsCommand, DefaultResult> deletePondsUseCase;

    public PondController(SessionExtractor sessionExtractor, UseCase<CreatePondsCommand, CreatePondsResult> createPondsUseCase, UseCase<ViewPondsQuery, ViewPondsResult> viewPondsUseCase, UseCase<UpdatePondsCommand, UpdatePondsResult> updatePondsUseCase, UseCase<DeletePondsCommand, DefaultResult> deletePondsUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createPondsUseCase = createPondsUseCase;
        this.viewPondsUseCase = viewPondsUseCase;
        this.updatePondsUseCase = updatePondsUseCase;
        this.deletePondsUseCase = deletePondsUseCase;
    }

    @Operation(
            summary = "Tambah kolam baru",
            description = """
                    Membuat kolam budidaya baru dalam organisasi.
                    
                    **Informasi Kolam:**
                    - `name` - Nama identifikasi kolam (contoh: "Kolam A1", "Kolam Utara")
                    - `location` - Lokasi fisik kolam dalam area budidaya
                    - `capacity` - Kapasitas maksimal bibit ikan (dalam ekor)
                    
                    **Ketentuan:**
                    - Nama kolam harus unik dalam organisasi
                    - Kapasitas minimal 100 ekor
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Kolam berhasil dibuat",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "880e8400-e29b-41d4-a716-446655440010",
                                                "name": "Kolam A1",
                                                "location": "Area Utara - Blok 1",
                                                "capacity": 5000,
                                                "createdAt": "2026-03-13T09:00:00"
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
                                              "message": "Nama kolam sudah digunakan",
                                              "data": null
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data kolam baru",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Kolam Ukuran Besar",
                                    summary = "Contoh kolam kapasitas besar",
                                    value = """
                                            {
                                              "name": "Kolam A1",
                                              "location": "Area Utara - Blok 1",
                                              "capacity": 5000
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Kolam Ukuran Sedang",
                                    summary = "Contoh kolam kapasitas sedang",
                                    value = """
                                            {
                                              "name": "Kolam B2",
                                              "location": "Area Selatan - Blok 2",
                                              "capacity": 2500
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Kolam Ukuran Kecil",
                                    summary = "Contoh kolam kapasitas kecil",
                                    value = """
                                            {
                                              "name": "Kolam Pembibitan",
                                              "location": "Area Timur - Greenhouse",
                                              "capacity": 1000
                                            }
                                            """
                            )
                    }
            )
    )
    @PostMapping()
    public ResponseEntity<?> createPonds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreatePondsRequest createPondsRequest) {
        UserSession userSession = sessionExtractor.extract(token);
        CreatePondsCommand createPondsCommand = new CreatePondsCommand(userSession, createPondsRequest.name(), createPondsRequest.location(), createPondsRequest.capacity());
        CreatePondsPresenter createPondsPresenter = new CreatePondsPresenter();
        createPondsUseCase.process(createPondsCommand, createPondsPresenter);
        return createPondsPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar kolam",
            description = """
                    Mengambil daftar semua kolam dalam organisasi.
                    
                    **Fitur:**
                    - Mendukung pagination
                    - Menampilkan semua kolam milik organisasi
                    - Diurutkan berdasarkan nama kolam
                    
                    **Informasi yang ditampilkan:**
                    - ID kolam
                    - Nama kolam
                    - Lokasi
                    - Kapasitas (ekor)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Berhasil mengambil daftar kolam",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Daftar Kolam",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "ponds": [
                                                  {
                                                    "id": "880e8400-e29b-41d4-a716-446655440010",
                                                    "name": "Kolam A1",
                                                    "location": "Area Utara - Blok 1",
                                                    "capacity": 5000
                                                  },
                                                  {
                                                    "id": "880e8400-e29b-41d4-a716-446655440011",
                                                    "name": "Kolam A2",
                                                    "location": "Area Utara - Blok 1",
                                                    "capacity": 5000
                                                  },
                                                  {
                                                    "id": "880e8400-e29b-41d4-a716-446655440012",
                                                    "name": "Kolam B1",
                                                    "location": "Area Selatan - Blok 2",
                                                    "capacity": 3000
                                                  },
                                                  {
                                                    "id": "880e8400-e29b-41d4-a716-446655440013",
                                                    "name": "Kolam Pembibitan",
                                                    "location": "Area Timur - Greenhouse",
                                                    "capacity": 1000
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
    public ResponseEntity<?> viewPonds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false) Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false) Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewPondsQuery viewPondsQuery = new ViewPondsQuery(userSession, page, limit);
        ViewPondsPresenter viewPondsPresenter = new ViewPondsPresenter();
        viewPondsUseCase.process(viewPondsQuery, viewPondsPresenter);
        return viewPondsPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Update data kolam",
            description = """
                    Mengupdate informasi kolam budidaya.
                    
                    **Field yang dapat diupdate:**
                    - `name` - Nama kolam
                    - `location` - Lokasi kolam
                    - `capacity` - Kapasitas kolam
                    
                    **Catatan:**
                    - Mengubah kapasitas tidak mempengaruhi bibit yang sudah ada
                    - Nama kolam harus tetap unik
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Kolam berhasil diupdate",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "880e8400-e29b-41d4-a716-446655440010",
                                                "name": "Kolam Premium A1",
                                                "location": "Area Utara - Blok 1 (Renovasi)",
                                                "capacity": 6000,
                                                "updatedAt": "2026-03-13T16:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kolam tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Kolam dengan ID tersebut tidak ditemukan",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data update kolam",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Contoh Request Update Kolam",
                            value = """
                                    {
                                      "name": "Kolam Premium A1",
                                      "location": "Area Utara - Blok 1 (Renovasi)",
                                      "capacity": 6000
                                    }
                                    """
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePonds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam yang akan diupdate", example = "880e8400-e29b-41d4-a716-446655440010") @PathVariable UUID id,
            @RequestBody UpdatePondsRequest updatePondsRequest) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdatePondsCommand updatePondsCommand = new UpdatePondsCommand(userSession, updatePondsRequest.name(), updatePondsRequest.location(), updatePondsRequest.capacity(), id);
        UpdatePondsPresenter updatePondsPresenter = new UpdatePondsPresenter();
        updatePondsUseCase.process(updatePondsCommand, updatePondsPresenter);
        return updatePondsPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus kolam",
            description = """
                    Menghapus kolam dari sistem.
                    
                    **Perhatian:**
                    - Kolam yang masih memiliki bibit aktif tidak dapat dihapus
                    - Hapus atau pindahkan semua bibit terlebih dahulu
                    - Data historis kolam akan tetap tersimpan untuk laporan
                    
                    **Ketentuan:**
                    - Hanya Owner dan Admin yang dapat menghapus kolam
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Kolam berhasil dihapus",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Kolam berhasil dihapus",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Kolam masih memiliki bibit aktif",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Tidak dapat menghapus kolam yang masih memiliki bibit aktif",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kolam tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Kolam dengan ID tersebut tidak ditemukan",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePonds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam yang akan dihapus", example = "880e8400-e29b-41d4-a716-446655440013") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeletePondsCommand deletePondsCommand = new DeletePondsCommand(userSession, id);
        DeletePondsPresenter deletePondsPresenter = new DeletePondsPresenter();
        deletePondsUseCase.process(deletePondsCommand, deletePondsPresenter);
        return deletePondsPresenter.getResponseEntity();
    }
}
