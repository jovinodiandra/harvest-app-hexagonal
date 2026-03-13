package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateGrowthRecordCommand;
import org.harvest.application.dto.command.DeleteGrowthRecordCommand;
import org.harvest.application.dto.command.UpdateGrowthRecordCommand;
import org.harvest.application.dto.query.ViewGrowthRecordQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateGrowthRecordRequest;
import org.harvest.springhttpadapter.dto.request.UpdateGrowthRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/growth-records")
@RestController
@Tag(name = "Growth Records", description = "API untuk manajemen catatan pertumbuhan ikan")
@SecurityRequirement(name = "bearerAuth")
public class GrowthRecordController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateGrowthRecordCommand, CreateGrowthRecordResult> createGrowthRecordUseCase;
    private final UseCase<ViewGrowthRecordQuery, ViewGrowthRecordResult> viewGrowthRecordUseCase;
    private final UseCase<UpdateGrowthRecordCommand, UpdateGrowthRecordResult> updateGrowthRecordUseCase;
    private final UseCase<DeleteGrowthRecordCommand, DefaultResult> deleteGrowthRecordUseCase;

    public GrowthRecordController(SessionExtractor sessionExtractor,
                                  UseCase<CreateGrowthRecordCommand, CreateGrowthRecordResult> createGrowthRecordUseCase,
                                  UseCase<ViewGrowthRecordQuery, ViewGrowthRecordResult> viewGrowthRecordUseCase,
                                  UseCase<UpdateGrowthRecordCommand, UpdateGrowthRecordResult> updateGrowthRecordUseCase,
                                  UseCase<DeleteGrowthRecordCommand, DefaultResult> deleteGrowthRecordUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createGrowthRecordUseCase = createGrowthRecordUseCase;
        this.viewGrowthRecordUseCase = viewGrowthRecordUseCase;
        this.updateGrowthRecordUseCase = updateGrowthRecordUseCase;
        this.deleteGrowthRecordUseCase = deleteGrowthRecordUseCase;
    }

    @Operation(
            summary = "Tambah catatan pertumbuhan baru",
            description = "Mencatat data pertumbuhan ikan (panjang dan berat rata-rata) untuk kolam tertentu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan pertumbuhan berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createGrowthRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateGrowthRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateGrowthRecordCommand command = new CreateGrowthRecordCommand(userSession, request.pondsId(), null, request.recordDate(), request.averageLength(), request.averageWeight());
        CreateGrowthRecordPresenter presenter = new CreateGrowthRecordPresenter();
        createGrowthRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan pertumbuhan",
            description = "Mengambil daftar catatan pertumbuhan untuk kolam tertentu dengan pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan pertumbuhan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewGrowthRecords(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam", required = true) @RequestParam UUID pondsId,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewGrowthRecordQuery query = new ViewGrowthRecordQuery(userSession, pondsId, page, limit);
        ViewGrowthRecordPresenter presenter = new ViewGrowthRecordPresenter();
        viewGrowthRecordUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan pertumbuhan",
            description = "Mengupdate informasi catatan pertumbuhan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan pertumbuhan berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan pertumbuhan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrowthRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan pertumbuhan yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateGrowthRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateGrowthRecordCommand command = new UpdateGrowthRecordCommand(userSession, request.pondsId(), id, request.recordDate(), request.averageLength(), request.averageWeight());
        UpdateGrowthRecordPresenter presenter = new UpdateGrowthRecordPresenter();
        updateGrowthRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan pertumbuhan",
            description = "Menghapus catatan pertumbuhan dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan pertumbuhan berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan pertumbuhan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrowthRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan pertumbuhan yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteGrowthRecordCommand command = new DeleteGrowthRecordCommand(userSession, id);
        DeleteGrowthRecordPresenter presenter = new DeleteGrowthRecordPresenter();
        deleteGrowthRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
