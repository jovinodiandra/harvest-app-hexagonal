package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateFeedScheduleCommand;
import org.harvest.application.dto.command.DeleteFeedScheduleCommand;
import org.harvest.application.dto.command.UpdateFeedScheduleCommand;
import org.harvest.application.dto.query.ViewFeedScheduleQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateFeedScheduleRequest;
import org.harvest.springhttpadapter.dto.request.UpdateFeedScheduleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/feed-schedules")
@RestController
@Tag(name = "Feed Schedules", description = "API untuk manajemen jadwal pemberian pakan")
@SecurityRequirement(name = "bearerAuth")
public class FeedScheduleController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateFeedScheduleCommand, CreateFeedScheduleResult> createFeedScheduleUseCase;
    private final UseCase<ViewFeedScheduleQuery, ViewFeedScheduleResult> viewFeedScheduleUseCase;
    private final UseCase<UpdateFeedScheduleCommand, UpdateFeedScheduleResult> updateFeedScheduleUseCase;
    private final UseCase<DeleteFeedScheduleCommand, DefaultResult> deleteFeedScheduleUseCase;

    public FeedScheduleController(SessionExtractor sessionExtractor,
                                  UseCase<CreateFeedScheduleCommand, CreateFeedScheduleResult> createFeedScheduleUseCase,
                                  UseCase<ViewFeedScheduleQuery, ViewFeedScheduleResult> viewFeedScheduleUseCase,
                                  UseCase<UpdateFeedScheduleCommand, UpdateFeedScheduleResult> updateFeedScheduleUseCase,
                                  UseCase<DeleteFeedScheduleCommand, DefaultResult> deleteFeedScheduleUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createFeedScheduleUseCase = createFeedScheduleUseCase;
        this.viewFeedScheduleUseCase = viewFeedScheduleUseCase;
        this.updateFeedScheduleUseCase = updateFeedScheduleUseCase;
        this.deleteFeedScheduleUseCase = deleteFeedScheduleUseCase;
    }

    @Operation(
            summary = "Tambah jadwal pakan baru",
            description = """
                    Menambahkan jadwal pemberian pakan untuk kolam.
                    
                    **Informasi Jadwal:**
                    - `pondsId` - UUID kolam
                    - `feedType` - Jenis pakan (pellet, cacing, dll)
                    - `feedAmount` - Jumlah pakan dalam kg
                    - `feedTime` - Waktu pemberian pakan
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jadwal pakan berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createFeedSchedule(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedScheduleRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateFeedScheduleCommand command = new CreateFeedScheduleCommand(userSession, request.pondsId(), null, request.feedType(), request.feedAmount(), request.feedTime());
        CreateFeedSchedulePresenter presenter = new CreateFeedSchedulePresenter();
        createFeedScheduleUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar jadwal pakan",
            description = "Mengambil daftar semua jadwal pemberian pakan dalam organisasi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar jadwal pakan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewFeedSchedules(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFeedScheduleQuery query = new ViewFeedScheduleQuery(userSession, page, limit);
        ViewFeedSchedulePresenter presenter = new ViewFeedSchedulePresenter();
        viewFeedScheduleUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update jadwal pakan",
            description = "Mengupdate informasi jadwal pemberian pakan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jadwal pakan berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Jadwal pakan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedSchedule(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID jadwal pakan yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateFeedScheduleRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateFeedScheduleCommand command = new UpdateFeedScheduleCommand(userSession, request.pondsId(), id, request.feedType(), request.feedAmount(), request.feedTime());
        UpdateFeedSchedulePresenter presenter = new UpdateFeedSchedulePresenter();
        updateFeedScheduleUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus jadwal pakan",
            description = "Menghapus jadwal pemberian pakan dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jadwal pakan berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Jadwal pakan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedSchedule(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID jadwal pakan yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteFeedScheduleCommand command = new DeleteFeedScheduleCommand(userSession, id);
        DeleteFeedSchedulePresenter presenter = new DeleteFeedSchedulePresenter();
        deleteFeedScheduleUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
