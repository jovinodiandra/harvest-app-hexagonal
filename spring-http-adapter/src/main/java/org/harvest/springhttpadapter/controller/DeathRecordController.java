package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateDeathRecordCommand;
import org.harvest.application.dto.command.DeleteDeathRecordCommand;
import org.harvest.application.dto.command.UpdateDeathRecordCommand;
import org.harvest.application.dto.query.ViewDeathRecordQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateDeathRecordRequest;
import org.harvest.springhttpadapter.dto.request.UpdateDeathRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/death-records")
@RestController
@Tag(name = "Death Records", description = "API untuk manajemen catatan kematian ikan")
@SecurityRequirement(name = "bearerAuth")
public class DeathRecordController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateDeathRecordCommand, CreateDeathRecordResult> createDeathRecordUseCase;
    private final UseCase<ViewDeathRecordQuery, ViewDeathRecordResult> viewDeathRecordUseCase;
    private final UseCase<UpdateDeathRecordCommand, UpdateDeathRecordResult> updateDeathRecordUseCase;
    private final UseCase<DeleteDeathRecordCommand, DefaultResult> deleteDeathRecordUseCase;

    public DeathRecordController(SessionExtractor sessionExtractor,
                                 UseCase<CreateDeathRecordCommand, CreateDeathRecordResult> createDeathRecordUseCase,
                                 UseCase<ViewDeathRecordQuery, ViewDeathRecordResult> viewDeathRecordUseCase,
                                 UseCase<UpdateDeathRecordCommand, UpdateDeathRecordResult> updateDeathRecordUseCase,
                                 UseCase<DeleteDeathRecordCommand, DefaultResult> deleteDeathRecordUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createDeathRecordUseCase = createDeathRecordUseCase;
        this.viewDeathRecordUseCase = viewDeathRecordUseCase;
        this.updateDeathRecordUseCase = updateDeathRecordUseCase;
        this.deleteDeathRecordUseCase = deleteDeathRecordUseCase;
    }

    @Operation(
            summary = "Tambah catatan kematian baru",
            description = """
                    Mencatat kematian ikan di kolam.
                    
                    **Informasi yang diperlukan:**
                    - `pondsId` - UUID kolam
                    - `recordDate` - Tanggal pencatatan
                    - `deathCount` - Jumlah ikan yang mati
                    - `notes` - Catatan tambahan (opsional)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kematian berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createDeathRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateDeathRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateDeathRecordCommand command = new CreateDeathRecordCommand(userSession, request.pondsId(), request.recordDate(), request.deathCount(), request.notes());
        CreateDeathRecordPresenter presenter = new CreateDeathRecordPresenter();
        createDeathRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan kematian",
            description = "Mengambil daftar catatan kematian ikan per kolam dengan pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan kematian"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewDeathRecords(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam", required = true) @RequestParam UUID pondsId,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewDeathRecordQuery query = new ViewDeathRecordQuery(userSession, pondsId, page, limit);
        ViewDeathRecordPresenter presenter = new ViewDeathRecordPresenter();
        viewDeathRecordUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan kematian",
            description = "Mengupdate informasi catatan kematian ikan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kematian berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan kematian tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeathRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan kematian yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateDeathRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateDeathRecordCommand command = new UpdateDeathRecordCommand(userSession, request.pondsId(), id, request.recordDate(), request.deathCount(), request.notes());
        UpdateDeathRecordPresenter presenter = new UpdateDeathRecordPresenter();
        updateDeathRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan kematian",
            description = "Menghapus catatan kematian ikan dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kematian berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan kematian tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeathRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan kematian yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteDeathRecordCommand command = new DeleteDeathRecordCommand(userSession, id);
        DeleteDeathRecordPresenter presenter = new DeleteDeathRecordPresenter();
        deleteDeathRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
