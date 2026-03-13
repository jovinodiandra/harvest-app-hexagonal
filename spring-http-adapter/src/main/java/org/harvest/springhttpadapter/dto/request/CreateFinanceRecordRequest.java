package org.harvest.springhttpadapter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.harvest.application.dto.value.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Request body untuk menambah catatan keuangan baru")
public record CreateFinanceRecordRequest(
        @Schema(description = "Jenis transaksi: INCOME (pemasukan) atau EXPENSE (pengeluaran)", example = "EXPENSE", required = true)
        TransactionType transactionType,
        
        @Schema(description = "Tanggal transaksi (format: YYYY-MM-DD)", example = "2026-03-13", required = true)
        LocalDate transactionDate,
        
        @Schema(description = "Jumlah nominal transaksi", example = "500000", required = true)
        BigDecimal amount,
        
        @Schema(description = "Kategori transaksi (pakan, bibit, operasional, penjualan, dll)", example = "Pakan", required = true)
        String category,
        
        @Schema(description = "Catatan tambahan", example = "Pembelian pakan pellet 10 karung")
        String notes
) {
}
