package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateFinanceRecordCommand;
import org.harvest.application.dto.command.DeleteFinanceRecordCommand;
import org.harvest.application.dto.command.UpdateFinanceRecordCommand;
import org.harvest.application.dto.query.ViewFinanceRecordQuery;
import org.harvest.application.dto.query.ViewFinanceReportQuery;
import org.harvest.application.dto.result.*;
import org.harvest.application.dto.value.TransactionType;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.*;
import org.harvest.springhttpadapter.dto.request.CreateFinanceRecordRequest;
import org.harvest.springhttpadapter.dto.request.UpdateFinanceRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/finance-records")
@RestController
@Tag(name = "Finance Records", description = "API untuk manajemen catatan keuangan budidaya")
@SecurityRequirement(name = "bearerAuth")
public class FinanceRecordController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateFinanceRecordCommand, CreateFinanceRecordResult> createFinanceRecordUseCase;
    private final UseCase<ViewFinanceRecordQuery, ViewFinanceRecordResult> viewFinanceRecordUseCase;
    private final UseCase<UpdateFinanceRecordCommand, UpdateFinanceRecordResult> updateFinanceRecordUseCase;
    private final UseCase<DeleteFinanceRecordCommand, DefaultResult> deleteFinanceRecordUseCase;
    private final UseCase<ViewFinanceReportQuery, ViewFinanceReportResult> viewFinanceReportUseCase;

    public FinanceRecordController(SessionExtractor sessionExtractor,
                                   UseCase<CreateFinanceRecordCommand, CreateFinanceRecordResult> createFinanceRecordUseCase,
                                   UseCase<ViewFinanceRecordQuery, ViewFinanceRecordResult> viewFinanceRecordUseCase,
                                   UseCase<UpdateFinanceRecordCommand, UpdateFinanceRecordResult> updateFinanceRecordUseCase,
                                   UseCase<DeleteFinanceRecordCommand, DefaultResult> deleteFinanceRecordUseCase,
                                   UseCase<ViewFinanceReportQuery, ViewFinanceReportResult> viewFinanceReportUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createFinanceRecordUseCase = createFinanceRecordUseCase;
        this.viewFinanceRecordUseCase = viewFinanceRecordUseCase;
        this.updateFinanceRecordUseCase = updateFinanceRecordUseCase;
        this.deleteFinanceRecordUseCase = deleteFinanceRecordUseCase;
        this.viewFinanceReportUseCase = viewFinanceReportUseCase;
    }

    @Operation(
            summary = "Tambah catatan keuangan baru",
            description = """
                    Mencatat transaksi keuangan (pemasukan atau pengeluaran).
                    
                    **Jenis Transaksi:**
                    - `INCOME` - Pemasukan (penjualan ikan, dll)
                    - `EXPENSE` - Pengeluaran (pakan, bibit, operasional, dll)
                    
                    **Kategori Umum:**
                    - Pemasukan: Penjualan, Subsidi, Lainnya
                    - Pengeluaran: Pakan, Bibit, Listrik, Tenaga Kerja, Obat, Lainnya
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan keuangan berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PostMapping()
    public ResponseEntity<?> createFinanceRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateFinanceRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateFinanceRecordCommand command = new CreateFinanceRecordCommand(userSession, request.transactionType(), request.transactionDate(), request.amount(), request.category(), request.notes());
        CreateFinanceRecordPresenter presenter = new CreateFinanceRecordPresenter();
        createFinanceRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar catatan keuangan",
            description = """
                    Mengambil daftar catatan keuangan dengan filter.
                    
                    **Filter yang tersedia:**
                    - Jenis transaksi (INCOME/EXPENSE)
                    - Rentang tanggal
                    - Pagination
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar catatan keuangan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewFinanceRecords(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Filter jenis transaksi: INCOME atau EXPENSE") @RequestParam(required = false) TransactionType transactionType,
            @Parameter(description = "Tanggal mulai (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Tanggal akhir (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false) Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false) Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFinanceRecordQuery query = new ViewFinanceRecordQuery(userSession, transactionType, startDate, endDate, page, limit);
        ViewFinanceRecordPresenter presenter = new ViewFinanceRecordPresenter();
        viewFinanceRecordUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat laporan keuangan",
            description = """
                    Mengambil laporan keuangan dengan ringkasan.
                    
                    **Informasi yang ditampilkan:**
                    - Daftar semua transaksi dalam periode
                    - Total pemasukan
                    - Total pengeluaran
                    - Saldo (pemasukan - pengeluaran)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil laporan keuangan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping("/report")
    public ResponseEntity<?> viewFinanceReport(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Tanggal mulai (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Tanggal akhir (format: YYYY-MM-DD)") @RequestParam(required = false) LocalDate endDate) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewFinanceReportQuery query = new ViewFinanceReportQuery(userSession, startDate, endDate);
        ViewFinanceReportPresenter presenter = new ViewFinanceReportPresenter();
        viewFinanceReportUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update catatan keuangan",
            description = "Mengupdate informasi catatan keuangan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan keuangan berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Catatan keuangan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFinanceRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan keuangan yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateFinanceRecordRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateFinanceRecordCommand command = new UpdateFinanceRecordCommand(userSession, id, request.transactionType(), request.amount(), request.category(), request.notes(), request.transactionDate());
        UpdateFinanceRecordPresenter presenter = new UpdateFinanceRecordPresenter();
        updateFinanceRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus catatan keuangan",
            description = "Menghapus catatan keuangan dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catatan keuangan berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Catatan keuangan tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFinanceRecord(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID catatan keuangan yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteFinanceRecordCommand command = new DeleteFinanceRecordCommand(userSession, id);
        DeleteFinanceRecordPresenter presenter = new DeleteFinanceRecordPresenter();
        deleteFinanceRecordUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
