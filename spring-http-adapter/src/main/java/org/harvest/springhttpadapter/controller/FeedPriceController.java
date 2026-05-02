package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateFeedPriceCommand;
import org.harvest.application.dto.command.DeleteFeedPriceCommand;
import org.harvest.application.dto.command.UpdateFeedPriceCommand;
import org.harvest.application.dto.query.ViewFeedPriceQuery;
import org.harvest.application.dto.result.CreateFeedPriceResult;
import org.harvest.application.dto.result.DeleteFeedPriceResult;
import org.harvest.application.dto.result.UpdateFeedPriceResult;
import org.harvest.application.dto.result.ViewFeedPriceResult;
import org.harvest.application.dto.value.FeedPriceStatus;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreateFeedPricePresenter;
import org.harvest.springhttpadapter.dto.presenter.DeleteFeedPricePresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdateFeedPricePresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewFeedPricePresenter;
import org.harvest.springhttpadapter.dto.request.CreateFeedPriceRequest;
import org.harvest.springhttpadapter.dto.request.UpdateFeedPriceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/feed-price")
@Tag(name = "Feed Price", description = "API untuk manajemen catatan harga pakan ")
@SecurityRequirement(name = "bearerAuth")
public class FeedPriceController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateFeedPriceCommand, CreateFeedPriceResult> createFeedPriceUseCase;
    private final UseCase<ViewFeedPriceQuery, ViewFeedPriceResult> viewFeedPriceUseCase;
    private final UseCase<UpdateFeedPriceCommand, UpdateFeedPriceResult> updateFeedPriceUseCase;
    private final UseCase<DeleteFeedPriceCommand, DeleteFeedPriceResult> deleteFeedPriceUseCase;

    public FeedPriceController(SessionExtractor sessionExtractor, UseCase<CreateFeedPriceCommand, CreateFeedPriceResult> createFeedPriceUseCase, UseCase<ViewFeedPriceQuery, ViewFeedPriceResult> viewFeedPriceUseCase, UseCase<UpdateFeedPriceCommand, UpdateFeedPriceResult> updateFeedPriceUseCase, UseCase<DeleteFeedPriceCommand, DeleteFeedPriceResult> deleteFeedPriceUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createFeedPriceUseCase = createFeedPriceUseCase;
        this.viewFeedPriceUseCase = viewFeedPriceUseCase;
        this.updateFeedPriceUseCase = updateFeedPriceUseCase;
        this.deleteFeedPriceUseCase = deleteFeedPriceUseCase;
    }

    @Operation(
            summary = "Tambah catatan harga pakan",
            description = "mencatat harga pakan ikan ")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "berhasil menambahkan catatan harga pakan"),
            @ApiResponse(responseCode = "400", description = "data tidak valid"),
            @ApiResponse(responseCode = "401", description = "token tidak valid"),
            @ApiResponse(responseCode = "403", description = "akses forbidden")
    })

    @PostMapping
    public ResponseEntity<?> createFeedPrice(
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFeedPriceRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateFeedPriceCommand command = new CreateFeedPriceCommand(
                userSession,
                request.feedName(),
                request.pricePerKiloGram(),
                request.effectiveDate(),
                request.description()
        );
        CreateFeedPricePresenter presenter = new CreateFeedPricePresenter();
        createFeedPriceUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "melihat daftar catatan harga pakan",
            description = "untuk melihat daftar harga pakan "
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "210", description = "berhasil mendapatkan daftar harga pakan"),
            @ApiResponse(responseCode = "400", description ="catatan harga pakan tidak ditemukan" ),
            @ApiResponse(responseCode = "401", description = "token tidak valid"),
    })

    @GetMapping
    public ResponseEntity<?> viewFeedPrice(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "ACTIVE")FeedPriceStatus status

            ) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFeedPriceQuery viewFeedPriceQuery = new ViewFeedPriceQuery(userSession,status, page, limit);
        ViewFeedPricePresenter viewFeedPricePresenter = new ViewFeedPricePresenter();
        viewFeedPriceUseCase.process(viewFeedPriceQuery, viewFeedPricePresenter);
        return viewFeedPricePresenter.getResponseEntity();
    }

    @Operation(
            summary = "update catatan harga pakan",
            description = "untuk mengupdate  harga pakan "
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "210", description = "berhasil mengupdate daftar harga pakan"),
            @ApiResponse(responseCode = "400", description =" harga pakan tidak ditemukan" ),
            @ApiResponse(responseCode = "401", description = "token tidak valid"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedPrice(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization")String token,
            @Parameter(description = "UUID feed price") @PathVariable UUID id,
            @RequestBody UpdateFeedPriceRequest request){
        UserSession userSession = sessionExtractor.extract(token);
        UpdateFeedPriceCommand command = new UpdateFeedPriceCommand(userSession,id, request.feedName(), request.pricePerKiloGram(), request.effectiveDate(),request.description());
        UpdateFeedPricePresenter presenter = new UpdateFeedPricePresenter();
        updateFeedPriceUseCase.process(command,presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "menghapus  catatan harga pakan",
            description = "untuk menghapus  harga pakan "
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "210", description = "berhasil delete  harga pakan"),
            @ApiResponse(responseCode = "400", description =" harga pakan tidak ditemukan" ),
            @ApiResponse(responseCode = "401", description = "token tidak valid"),
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedPrice(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID feed price") @PathVariable UUID id,
            @Parameter(description = "alasan untuk penghapusan") @RequestParam() String description){
        UserSession userSession = sessionExtractor.extract(token);
        DeleteFeedPriceCommand command = new DeleteFeedPriceCommand(userSession, id, description);
        DeleteFeedPricePresenter presenter = new DeleteFeedPricePresenter();
        deleteFeedPriceUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }


}
