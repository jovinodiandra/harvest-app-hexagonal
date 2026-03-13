package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateFeedReminderCommand;
import org.harvest.application.dto.command.DeleteFeedReminderCommand;
import org.harvest.application.dto.command.UpdateFeedReminderCommand;
import org.harvest.application.dto.query.ViewFeedRemindersQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateFeedReminderRequest;
import org.harvest.springhttpadapter.dto.request.UpdateFeedReminderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/feed-reminders")
@RestController
@Tag(name = "Feed Reminders", description = "API untuk manajemen pengingat pemberian pakan")
@SecurityRequirement(name = "bearerAuth")
public class FeedReminderController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateFeedReminderCommand, CreateFeedReminderResult> createFeedReminderUseCase;
    private final UseCase<ViewFeedRemindersQuery, ViewFeedRemindersResult> viewFeedRemindersUseCase;
    private final UseCase<UpdateFeedReminderCommand, UpdateFeedReminderResult> updateFeedReminderUseCase;
    private final UseCase<DeleteFeedReminderCommand, DefaultResult> deleteFeedReminderUseCase;

    public FeedReminderController(SessionExtractor sessionExtractor,
                                  UseCase<CreateFeedReminderCommand, CreateFeedReminderResult> createFeedReminderUseCase,
                                  UseCase<ViewFeedRemindersQuery, ViewFeedRemindersResult> viewFeedRemindersUseCase,
                                  UseCase<UpdateFeedReminderCommand, UpdateFeedReminderResult> updateFeedReminderUseCase,
                                  UseCase<DeleteFeedReminderCommand, DefaultResult> deleteFeedReminderUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createFeedReminderUseCase = createFeedReminderUseCase;
        this.viewFeedRemindersUseCase = viewFeedRemindersUseCase;
        this.updateFeedReminderUseCase = updateFeedReminderUseCase;
        this.deleteFeedReminderUseCase = deleteFeedReminderUseCase;
    }

    @Operation(summary = "Tambah pengingat pakan baru", description = "Menambahkan pengingat untuk pemberian pakan pada kolam tertentu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat pakan berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createFeedReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedReminderRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateFeedReminderCommand command = new CreateFeedReminderCommand(userSession, request.pondId(), request.reminderDate(), request.reminderTime(), request.feedType(), request.notes());
        CreateFeedReminderPresenter presenter = new CreateFeedReminderPresenter();
        createFeedReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Lihat daftar pengingat pakan", description = "Mengambil daftar pengingat pakan dengan filter kolam dan tanggal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar pengingat pakan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewFeedReminders(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kolam (opsional)") @RequestParam(required = false) UUID pondId,
            @Parameter(description = "Tanggal (opsional, format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate date) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFeedRemindersQuery query = new ViewFeedRemindersQuery(userSession, pondId, date);
        ViewFeedRemindersPresenter presenter = new ViewFeedRemindersPresenter();
        viewFeedRemindersUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Update pengingat pakan", description = "Mengupdate informasi pengingat pakan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat pakan berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Pengingat tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID pengingat pakan") @PathVariable UUID id,
            @RequestBody UpdateFeedReminderRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateFeedReminderCommand command = new UpdateFeedReminderCommand(userSession, id, request.pondId(), request.reminderDate(), request.reminderTime(), request.feedType(), request.notes());
        UpdateFeedReminderPresenter presenter = new UpdateFeedReminderPresenter();
        updateFeedReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(summary = "Hapus pengingat pakan", description = "Menghapus pengingat pakan dari sistem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pengingat pakan berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Pengingat tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedReminder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID pengingat pakan") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteFeedReminderCommand command = new DeleteFeedReminderCommand(userSession, id);
        DeleteFeedReminderPresenter presenter = new DeleteFeedReminderPresenter();
        deleteFeedReminderUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
