package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateDiseasesRecordCommand;
import org.harvest.application.dto.command.DeleteDiseasesRecordCommand;
import org.harvest.application.dto.command.UpdateDiseasesRecordCommand;
import org.harvest.application.dto.query.VIewDiseasesRecordQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateDiseasesRecordRequest;
import org.harvest.springhttpadapter.dto.request.UpdateDiseasesRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/diseases-records")
@RestController
@Tag(name = "Diseases Records", description = "API untuk manajemen catatan penyakit budidaya")
@SecurityRequirement(name = "bearerAuth")
public class DiseasesRecordController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateDiseasesRecordCommand, CreateDiseasesRecordResult> createDiseasesRecordUseCase;
    private final UseCase<VIewDiseasesRecordQuery, ViewDiseasesRecordResult> viewDiseasesRecordUseCase;
    private final UseCase<UpdateDiseasesRecordCommand, UpdateDiseasesRecordResult> updateDiseasesRecordUseCase;
    private final UseCase<DeleteDiseasesRecordCommand, DefaultResult> deleteDiseasesRecordUseCase;

    public DiseasesRecordController(SessionExtractor sessionExtractor,
                                    UseCase<CreateDiseasesRecordCommand, CreateDiseasesRecordResult> createDiseasesRecordUseCase,
                                    UseCase<VIewDiseasesRecordQuery, ViewDiseasesRecordResult> viewDiseasesRecordUseCase,
                                    UseCase<UpdateDiseasesRecordCommand, UpdateDiseasesRecordResult> updateDiseasesRecordUseCase,
                                    UseCase<DeleteDiseasesRecordCommand, DefaultResult> deleteDiseasesRecordUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createDiseasesRecordUseCase = createDiseasesRecordUseCase;
        this.viewDiseasesRecordUseCase = viewDiseasesRecordUseCase;
        this.updateDiseasesRecordUseCase = updateDiseasesRecordUseCase;
        this.deleteDiseasesRecordUseCase = deleteDiseasesRecordUseCase;
    }

    @Operation(
            summary = "Tambah catatan penyakit baru",
            description = "Mencatat penyakit yang terjadi pada kolam budidaya."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan penyakit berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createDiseasesRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateDiseasesRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateDiseasesRecordCommand command = new CreateDiseasesRecordCommand(
                userSession,
                request.pondId(),
                request.diseaseName(),
                request.symptoms(),
                request.infectedFishCount(),
                request.diseaseDate(),
                request.notes()
        );
        CreateDiseasesRecordPresenter presenter = new CreateDiseasesRecordPresenter();
        createDiseasesRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan penyakit",
            description = "Mengambil daftar catatan penyakit berdasarkan kolam dengan pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan penyakit"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewDiseasesRecords(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "ID kolam", required = true) @RequestParam UUID pondId,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        VIewDiseasesRecordQuery query = new VIewDiseasesRecordQuery(userSession, pondId, page, limit);
        ViewDiseasesRecordPresenter presenter = new ViewDiseasesRecordPresenter();
        viewDiseasesRecordUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan penyakit",
            description = "Mengupdate informasi catatan penyakit."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan penyakit berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan penyakit tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiseasesRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan penyakit yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateDiseasesRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateDiseasesRecordCommand command = new UpdateDiseasesRecordCommand(
                userSession,
                id,
                request.pondId(),
                request.diseaseName(),
                request.symptoms(),
                request.infectedFishCount(),
                request.diseaseDate(),
                request.notes()
        );
        UpdateDiseasesRecordPresenter presenter = new UpdateDiseasesRecordPresenter();
        updateDiseasesRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan penyakit",
            description = "Menghapus catatan penyakit dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan penyakit berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan penyakit tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiseasesRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan penyakit yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteDiseasesRecordCommand command = new DeleteDiseasesRecordCommand(userSession, id);
        DeleteDiseasesRecordPresenter presenter = new DeleteDiseasesRecordPresenter();
        deleteDiseasesRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
