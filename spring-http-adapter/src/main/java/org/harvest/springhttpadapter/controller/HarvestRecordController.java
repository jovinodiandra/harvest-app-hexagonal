package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateHarvestRecordCommand;
import org.harvest.application.dto.command.DeleteHarvestRecordCommand;
import org.harvest.application.dto.command.UpdateHarvestRecordCommand;
import org.harvest.application.dto.query.ViewHarvestRecordQuery;
import org.harvest.application.dto.query.ViewHarvestReportQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateHarvestRecordRequest;
import org.harvest.springhttpadapter.dto.request.UpdateHarvestRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/harvest-records")
@RestController
@Tag(name = "Harvest Records", description = "API untuk manajemen catatan panen ikan")
@SecurityRequirement(name = "bearerAuth")
public class HarvestRecordController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateHarvestRecordCommand, CreateHarvestRecordResult> createHarvestRecordUseCase;
    private final UseCase<ViewHarvestRecordQuery, ViewHarvestRecordResult> viewHarvestRecordUseCase;
    private final UseCase<UpdateHarvestRecordCommand, UpdateHarvestRecordResult> updateHarvestRecordUseCase;
    private final UseCase<DeleteHarvestRecordCommand, DefaultResult> deleteHarvestRecordUseCase;
    private final UseCase<ViewHarvestReportQuery, ViewHarvestReportResult> viewHarvestReportUseCase;

    public HarvestRecordController(SessionExtractor sessionExtractor,
                                   UseCase<CreateHarvestRecordCommand, CreateHarvestRecordResult> createHarvestRecordUseCase,
                                   UseCase<ViewHarvestRecordQuery, ViewHarvestRecordResult> viewHarvestRecordUseCase,
                                   UseCase<UpdateHarvestRecordCommand, UpdateHarvestRecordResult> updateHarvestRecordUseCase,
                                   UseCase<DeleteHarvestRecordCommand, DefaultResult> deleteHarvestRecordUseCase,
                                   UseCase<ViewHarvestReportQuery, ViewHarvestReportResult> viewHarvestReportUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createHarvestRecordUseCase = createHarvestRecordUseCase;
        this.viewHarvestRecordUseCase = viewHarvestRecordUseCase;
        this.updateHarvestRecordUseCase = updateHarvestRecordUseCase;
        this.deleteHarvestRecordUseCase = deleteHarvestRecordUseCase;
        this.viewHarvestReportUseCase = viewHarvestReportUseCase;
    }

    @Operation(
            summary = "Tambah catatan panen baru",
            description = """
                    Mencatat hasil panen ikan di kolam.
                    
                    **Informasi yang diperlukan:**
                    - `pondId` - UUID kolam
                    - `harvestDate` - Tanggal panen
                    - `harvestFishCount` - Jumlah ikan yang dipanen
                    - `totalWeight` - Total berat panen (kg)
                    - `notes` - Catatan tambahan (opsional)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan panen berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createHarvestRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateHarvestRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateHarvestRecordCommand command = new CreateHarvestRecordCommand(userSession, request.pondId(), request.harvestDate(), request.harvestFishCount(), request.totalWeight(), request.notes());
        CreateHarvestRecordPresenter presenter = new CreateHarvestRecordPresenter();
        createHarvestRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan panen",
            description = "Mengambil daftar catatan panen dengan pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan panen"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewHarvestRecords(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewHarvestRecordQuery query = new ViewHarvestRecordQuery(userSession, page, limit);
        ViewHarvestRecordPresenter presenter = new ViewHarvestRecordPresenter();
        viewHarvestRecordUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan panen",
            description = "Mengupdate informasi catatan panen."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan panen berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan panen tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHarvestRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan panen yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateHarvestRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateHarvestRecordCommand command = new UpdateHarvestRecordCommand(userSession, id, request.pondId(), request.harvestDate(), request.harvestFishCount(), request.totalWeight(), request.notes());
        UpdateHarvestRecordPresenter presenter = new UpdateHarvestRecordPresenter();
        updateHarvestRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan panen",
            description = "Menghapus catatan panen dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan panen berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan panen tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHarvestRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan panen yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteHarvestRecordCommand command = new DeleteHarvestRecordCommand(userSession, id);
        DeleteHarvestRecordPresenter presenter = new DeleteHarvestRecordPresenter();
        deleteHarvestRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat laporan panen",
            description = "Mengambil laporan panen berdasarkan filter kolam dan periode tanggal."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil laporan panen"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping("/report")
    public ResponseEntity<?> viewHarvestReport(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam (opsional, kosongkan untuk semua kolam)") @RequestParam(required = false) UUID pondId,
            @Parameter(description = "Tanggal awal periode (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Tanggal akhir periode (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate endDate) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewHarvestReportQuery query = new ViewHarvestReportQuery(userSession, pondId, startDate, endDate);
        ViewHarvestReportPresenter presenter = new ViewHarvestReportPresenter();
        viewHarvestReportUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }
}
