package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateHarvestReminderCommand;
import org.harvest.application.dto.command.DeleteHarvestReminderCommand;
import org.harvest.application.dto.command.UpdateHarvestReminderCommand;
import org.harvest.application.dto.query.ViewHarvestReminderQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateHarvestReminderRequest;
import org.harvest.springhttpadapter.dto.request.UpdateHarvestReminderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/harvest-reminders")
@RestController
@Tag(name = "Harvest Reminders", description = "API untuk manajemen pengingat panen")
@SecurityRequirement(name = "bearerAuth")
public class HarvestReminderController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateHarvestReminderCommand, CreateHarvestReminderResult> createHarvestReminderUseCase;
    private final UseCase<ViewHarvestReminderQuery, ViewHarvestReminderResult> viewHarvestReminderUseCase;
    private final UseCase<UpdateHarvestReminderCommand, UpdateHarvestReminderResult> updateHarvestReminderUseCase;
    private final UseCase<DeleteHarvestReminderCommand, DefaultResult> deleteHarvestReminderUseCase;

    public HarvestReminderController(SessionExtractor sessionExtractor,
                                     UseCase<CreateHarvestReminderCommand, CreateHarvestReminderResult> createHarvestReminderUseCase,
                                     UseCase<ViewHarvestReminderQuery, ViewHarvestReminderResult> viewHarvestReminderUseCase,
                                     UseCase<UpdateHarvestReminderCommand, UpdateHarvestReminderResult> updateHarvestReminderUseCase,
                                     UseCase<DeleteHarvestReminderCommand, DefaultResult> deleteHarvestReminderUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createHarvestReminderUseCase = createHarvestReminderUseCase;
        this.viewHarvestReminderUseCase = viewHarvestReminderUseCase;
        this.updateHarvestReminderUseCase = updateHarvestReminderUseCase;
        this.deleteHarvestReminderUseCase = deleteHarvestReminderUseCase;
    }

    @Operation(summary = "Tambah pengingat panen baru", description = "Menambahkan pengingat untuk panen pada kolam tertentu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat panen berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createHarvestReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateHarvestReminderRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateHarvestReminderCommand command = new CreateHarvestReminderCommand(userSession, request.pondId(), request.reminderDate(), request.reminderTime(), request.notes());
        CreateHarvestReminderPresenter presenter = new CreateHarvestReminderPresenter();
        createHarvestReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Lihat daftar pengingat panen", description = "Mengambil daftar pengingat panen dengan filter kolam dan tanggal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar pengingat panen"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewHarvestReminders(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam (opsional)") @RequestParam(required = false) UUID pondId,
            @Parameter(description = "Tanggal (opsional, format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate date) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewHarvestReminderQuery query = new ViewHarvestReminderQuery(userSession, pondId, date);
        ViewHarvestReminderPresenter presenter = new ViewHarvestReminderPresenter();
        viewHarvestReminderUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Update pengingat panen", description = "Mengupdate informasi pengingat panen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat panen berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Pengingat tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHarvestReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID pengingat panen") @PathVariable UUID id,
            @RequestBody UpdateHarvestReminderRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateHarvestReminderCommand command = new UpdateHarvestReminderCommand(userSession, id, request.pondId(), request.reminderDate(), request.reminderTime(), request.notes());
        UpdateHarvestReminderPresenter presenter = new UpdateHarvestReminderPresenter();
        updateHarvestReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Hapus pengingat panen", description = "Menghapus pengingat panen dari sistem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat panen berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Pengingat tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHarvestReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID pengingat panen") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteHarvestReminderCommand command = new DeleteHarvestReminderCommand(userSession, id);
        DeleteHarvestReminderPresenter presenter = new DeleteHarvestReminderPresenter();
        deleteHarvestReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
