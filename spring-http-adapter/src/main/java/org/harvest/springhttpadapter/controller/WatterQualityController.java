package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateWatterQualityCommand;
import org.harvest.application.dto.command.DeleteWatterQualityCommand;
import org.harvest.application.dto.command.UpdateWatterQualityCommand;
import org.harvest.application.dto.query.ViewWatterQualityQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateWatterQualityRequest;
import org.harvest.springhttpadapter.dto.request.UpdateWatterQualityRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/watter-quality")
@RestController
@Tag(name = "Water Quality", description = "API untuk manajemen catatan kualitas air")
@SecurityRequirement(name = "bearerAuth")
public class WatterQualityController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateWatterQualityCommand, CreateWatterQualityResult> createWatterQualityUseCase;
    private final UseCase<ViewWatterQualityQuery, ViewWatterQualityResult> viewWatterQualityUseCase;
    private final UseCase<UpdateWatterQualityCommand, UpdateWatterQualityResult> updateWatterQualityUseCase;
    private final UseCase<DeleteWatterQualityCommand, DefaultResult> deleteWatterQualityUseCase;

    public WatterQualityController(SessionExtractor sessionExtractor,
                                   UseCase<CreateWatterQualityCommand, CreateWatterQualityResult> createWatterQualityUseCase,
                                   UseCase<ViewWatterQualityQuery, ViewWatterQualityResult> viewWatterQualityUseCase,
                                   UseCase<UpdateWatterQualityCommand, UpdateWatterQualityResult> updateWatterQualityUseCase,
                                   UseCase<DeleteWatterQualityCommand, DefaultResult> deleteWatterQualityUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createWatterQualityUseCase = createWatterQualityUseCase;
        this.viewWatterQualityUseCase = viewWatterQualityUseCase;
        this.updateWatterQualityUseCase = updateWatterQualityUseCase;
        this.deleteWatterQualityUseCase = deleteWatterQualityUseCase;
    }

    @Operation(
            summary = "Tambah catatan kualitas air baru",
            description = "Mencatat data kualitas air (pH, suhu, oksigen terlarut) untuk kolam tertentu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kualitas air berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createWatterQuality(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateWatterQualityRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateWatterQualityCommand command = new CreateWatterQualityCommand(userSession, request.pondsId(), request.recordDate(), request.ph(), request.temperature(), request.dissolvedOxygen());
        CreateWatterQualityPresenter presenter = new CreateWatterQualityPresenter();
        createWatterQualityUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan kualitas air",
            description = "Mengambil daftar catatan kualitas air untuk kolam tertentu dengan pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan kualitas air"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewWatterQualities(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam", required = true) @RequestParam UUID pondsId,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewWatterQualityQuery query = new ViewWatterQualityQuery(userSession, page, limit, pondsId);
        ViewWatterQualityPresenter presenter = new ViewWatterQualityPresenter();
        viewWatterQualityUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan kualitas air",
            description = "Mengupdate informasi catatan kualitas air."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kualitas air berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan kualitas air tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWatterQuality(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan kualitas air yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateWatterQualityRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateWatterQualityCommand command = new UpdateWatterQualityCommand(userSession, id, request.pondsId(), request.recordDate(), request.ph(), request.temperature(), request.dissolvedOxygen());
        UpdateWatterQualityPresenter presenter = new UpdateWatterQualityPresenter();
        updateWatterQualityUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan kualitas air",
            description = "Menghapus catatan kualitas air dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan kualitas air berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan kualitas air tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWatterQuality(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan kualitas air yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteWatterQualityCommand command = new DeleteWatterQualityCommand(userSession, id);
        DeleteWatterQualityPresenter presenter = new DeleteWatterQualityPresenter();
        deleteWatterQualityUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
