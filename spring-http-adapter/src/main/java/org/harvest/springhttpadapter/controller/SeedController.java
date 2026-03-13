package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateSeedCommand;
import org.harvest.application.dto.command.DeleteSeedCommand;
import org.harvest.application.dto.command.UpdateSeedCommand;
import org.harvest.application.dto.query.ViewSeedQuery;
import org.harvest.application.dto.result.CreateSeedResult;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdateSeedResult;
import org.harvest.application.dto.result.ViewSeedResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreateSeedPresenter;
import org.harvest.springhttpadapter.dto.presenter.DeleteSeedPresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdateSeedPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewSeedPresenter;
import org.harvest.springhttpadapter.dto.request.CreateSeedRequest;
import org.harvest.springhttpadapter.dto.request.UpdateSeedRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/seeds")
@RestController
@Tag(name = "Seeds", description = "API untuk manajemen bibit ikan lele")
@SecurityRequirement(name = "bearerAuth")
public class SeedController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateSeedCommand, CreateSeedResult> createSeedUseCase;
    private final UseCase<ViewSeedQuery, ViewSeedResult> viewSeedUseCase;
    private final UseCase<UpdateSeedCommand, UpdateSeedResult> updateSeedUseCase;
    private final UseCase<DeleteSeedCommand, DefaultResult> deleteSeedUseCase;

    public SeedController(SessionExtractor sessionExtractor, UseCase<CreateSeedCommand, CreateSeedResult> createSeedUseCase, UseCase<ViewSeedQuery, ViewSeedResult> viewSeedUseCase, UseCase<UpdateSeedCommand, UpdateSeedResult> updateSeedUseCase, UseCase<DeleteSeedCommand, DefaultResult> deleteSeedUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createSeedUseCase = createSeedUseCase;
        this.viewSeedUseCase = viewSeedUseCase;
        this.updateSeedUseCase = updateSeedUseCase;
        this.deleteSeedUseCase = deleteSeedUseCase;
    }

    @Operation(
            summary = "Tambah bibit baru",
            description = """
                    Mencatat penambahan bibit ikan ke dalam kolam.
                    
                    **Informasi Bibit:**
                    - `pondsId` - UUID kolam tujuan
                    - `type` - Jenis/varietas bibit lele
                    - `quantity` - Jumlah bibit (ekor)
                    - `stockDate` - Tanggal penebaran bibit
                    
                    **Jenis Bibit Lele yang Umum:**
                    - Sangkuriang - Pertumbuhan cepat, tahan penyakit
                    - Dumbo - Ukuran besar, cocok untuk pembesaran
                    - Phyton - Hybrid unggul, produktivitas tinggi
                    - Mutiara - Lokal berkualitas
                    
                    **Validasi:**
                    - Kolam harus ada dan milik organisasi yang sama
                    - Jumlah tidak boleh melebihi kapasitas kolam yang tersisa
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bibit berhasil ditambahkan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "990e8400-e29b-41d4-a716-446655440020",
                                                "pondsId": "880e8400-e29b-41d4-a716-446655440010",
                                                "type": "Sangkuriang",
                                                "quantity": 2000,
                                                "stockDate": "2026-03-13",
                                                "createdAt": "2026-03-13T08:00:00"
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
                            examples = {
                                    @ExampleObject(
                                            name = "Kapasitas Penuh",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "message": "Jumlah bibit melebihi kapasitas kolam yang tersedia",
                                                      "data": null
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Kolam Tidak Ditemukan",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "message": "Kolam dengan ID tersebut tidak ditemukan",
                                                      "data": null
                                                    }
                                                    """
                                    )
                            }
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
            description = "Data bibit baru",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Bibit Sangkuriang",
                                    summary = "Contoh bibit jenis Sangkuriang",
                                    value = """
                                            {
                                              "pondsId": "880e8400-e29b-41d4-a716-446655440010",
                                              "type": "Sangkuriang",
                                              "quantity": 2000,
                                              "stockDate": "2026-03-13"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Bibit Dumbo",
                                    summary = "Contoh bibit jenis Dumbo",
                                    value = """
                                            {
                                              "pondsId": "880e8400-e29b-41d4-a716-446655440011",
                                              "type": "Dumbo",
                                              "quantity": 1500,
                                              "stockDate": "2026-03-15"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Bibit Phyton",
                                    summary = "Contoh bibit jenis Phyton hybrid",
                                    value = """
                                            {
                                              "pondsId": "880e8400-e29b-41d4-a716-446655440012",
                                              "type": "Phyton",
                                              "quantity": 3000,
                                              "stockDate": "2026-03-20"
                                            }
                                            """
                            )
                    }
            )
    )
    @PostMapping()
    public ResponseEntity<?> createSeeds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateSeedRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateSeedCommand createSeedCommand = new CreateSeedCommand(userSession, request.pondsId(), request.type(), request.quantity(), request.stockDate());
        CreateSeedPresenter createSeedPresenter = new CreateSeedPresenter();
        createSeedUseCase.process(createSeedCommand, createSeedPresenter);
        return createSeedPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar bibit",
            description = """
                    Mengambil daftar semua bibit dalam organisasi.
                    
                    **Fitur:**
                    - Mendukung pagination
                    - Menampilkan semua bibit dari semua kolam
                    - Diurutkan berdasarkan tanggal penebaran (terbaru lebih dulu)
                    
                    **Informasi yang ditampilkan:**
                    - ID bibit
                    - ID kolam
                    - Jenis bibit
                    - Jumlah
                    - Tanggal penebaran
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Berhasil mengambil daftar bibit",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Daftar Bibit",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "seeds": [
                                                  {
                                                    "id": "990e8400-e29b-41d4-a716-446655440020",
                                                    "pondId": "880e8400-e29b-41d4-a716-446655440010",
                                                    "type": "Sangkuriang",
                                                    "quantity": 2000,
                                                    "stockDate": "2026-03-13"
                                                  },
                                                  {
                                                    "id": "990e8400-e29b-41d4-a716-446655440021",
                                                    "pondId": "880e8400-e29b-41d4-a716-446655440011",
                                                    "type": "Dumbo",
                                                    "quantity": 1500,
                                                    "stockDate": "2026-03-10"
                                                  },
                                                  {
                                                    "id": "990e8400-e29b-41d4-a716-446655440022",
                                                    "pondId": "880e8400-e29b-41d4-a716-446655440010",
                                                    "type": "Phyton",
                                                    "quantity": 2500,
                                                    "stockDate": "2026-03-05"
                                                  },
                                                  {
                                                    "id": "990e8400-e29b-41d4-a716-446655440023",
                                                    "pondId": "880e8400-e29b-41d4-a716-446655440012",
                                                    "type": "Mutiara",
                                                    "quantity": 1000,
                                                    "stockDate": "2026-03-01"
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
    public ResponseEntity<?> viewSeeds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false) Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false) Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewSeedQuery viewSeedQuery = new ViewSeedQuery(userSession, page, limit);
        ViewSeedPresenter viewSeedPresenter = new ViewSeedPresenter();
        viewSeedUseCase.process(viewSeedQuery, viewSeedPresenter);
        return viewSeedPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Update data bibit",
            description = """
                    Mengupdate informasi bibit ikan.
                    
                    **Field yang dapat diupdate:**
                    - `pondId` - Pindah bibit ke kolam lain
                    - `type` - Koreksi jenis bibit
                    - `quantity` - Update jumlah (misal: kematian/penambahan)
                    - `stockDate` - Koreksi tanggal penebaran
                    
                    **Catatan:**
                    - Perubahan jumlah akan mempengaruhi kapasitas kolam
                    - Memindahkan bibit akan mengupdate kapasitas kedua kolam
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bibit berhasil diupdate",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "990e8400-e29b-41d4-a716-446655440020",
                                                "pondsId": "880e8400-e29b-41d4-a716-446655440010",
                                                "type": "Sangkuriang Premium",
                                                "quantity": 1850,
                                                "stockDate": "2026-03-13",
                                                "updatedAt": "2026-03-13T17:00:00"
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
                                              "message": "Kapasitas kolam tujuan tidak mencukupi",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bibit tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Bibit dengan ID tersebut tidak ditemukan",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data update bibit",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Update Jumlah (Kematian)",
                                    summary = "Contoh update karena kematian bibit",
                                    value = """
                                            {
                                              "id": "990e8400-e29b-41d4-a716-446655440020",
                                              "pondId": "880e8400-e29b-41d4-a716-446655440010",
                                              "type": "Sangkuriang",
                                              "quantity": 1850,
                                              "stockDate": "2026-03-13"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Pindah Kolam",
                                    summary = "Contoh memindahkan bibit ke kolam lain",
                                    value = """
                                            {
                                              "id": "990e8400-e29b-41d4-a716-446655440020",
                                              "pondId": "880e8400-e29b-41d4-a716-446655440011",
                                              "type": "Sangkuriang",
                                              "quantity": 1850,
                                              "stockDate": "2026-03-13"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Koreksi Jenis",
                                    summary = "Contoh koreksi jenis bibit",
                                    value = """
                                            {
                                              "id": "990e8400-e29b-41d4-a716-446655440020",
                                              "pondId": "880e8400-e29b-41d4-a716-446655440010",
                                              "type": "Sangkuriang Premium",
                                              "quantity": 2000,
                                              "stockDate": "2026-03-13"
                                            }
                                            """
                            )
                    }
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeeds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID bibit yang akan diupdate", example = "990e8400-e29b-41d4-a716-446655440020") @PathVariable UUID id,
            @RequestBody UpdateSeedRequest updateSeedRequest) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateSeedCommand updateSeedCommand = new UpdateSeedCommand(userSession, updateSeedRequest.pondId(), id, updateSeedRequest.type(), updateSeedRequest.quantity(), updateSeedRequest.stockDate());
        UpdateSeedPresenter updateSeedPresenter = new UpdateSeedPresenter();
        updateSeedUseCase.process(updateSeedCommand, updateSeedPresenter);
        return updateSeedPresenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus bibit",
            description = """
                    Menghapus record bibit dari sistem.
                    
                    **Penggunaan:**
                    - Mencatat bibit yang sudah dipanen
                    - Menghapus entry yang salah input
                    - Mencatat total loss (kematian 100%)
                    
                    **Catatan:**
                    - Data historis akan tetap tersimpan untuk laporan
                    - Kapasitas kolam akan diupdate setelah penghapusan
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Bibit berhasil dihapus",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Bibit berhasil dihapus",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bibit tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Bibit dengan ID tersebut tidak ditemukan",
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeeds(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID bibit yang akan dihapus", example = "990e8400-e29b-41d4-a716-446655440023") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteSeedCommand deleteSeedCommand = new DeleteSeedCommand(userSession, id);
        DeleteSeedPresenter deleteSeedPresenter = new DeleteSeedPresenter();
        deleteSeedUseCase.process(deleteSeedCommand, deleteSeedPresenter);
        return deleteSeedPresenter.getResponseEntity();
    }
}
