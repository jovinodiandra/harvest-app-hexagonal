package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateSupplierCommand;
import org.harvest.application.dto.command.DeleteSupplierCommand;
import org.harvest.application.dto.command.UpdateSupplierCommand;
import org.harvest.application.dto.query.ViewSupplierQuery;
import org.harvest.application.dto.result.CreateSupplierResult;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdateSupplierResult;
import org.harvest.application.dto.result.ViewSupplierResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreateSupplierPresenter;
import org.harvest.springhttpadapter.dto.presenter.DeleteSupplierPresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdateSupplierPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewSupplierPresenter;
import org.harvest.springhttpadapter.dto.request.CreateSupplierRequest;
import org.harvest.springhttpadapter.dto.request.UpdateSupplierRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/suppliers")
@RestController
@Tag(name = "Suppliers", description = "API untuk manajemen supplier/pemasok")
@SecurityRequirement(name = "bearerAuth")
public class SupplierController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateSupplierCommand, CreateSupplierResult> createSupplierUseCase;
    private final UseCase<ViewSupplierQuery, ViewSupplierResult> viewSupplierUseCase;
    private final UseCase<UpdateSupplierCommand, UpdateSupplierResult> updateSupplierUseCase;
    private final UseCase<DeleteSupplierCommand, DefaultResult> deleteSupplierUseCase;

    public SupplierController(SessionExtractor sessionExtractor,
                              UseCase<CreateSupplierCommand, CreateSupplierResult> createSupplierUseCase,
                              UseCase<ViewSupplierQuery, ViewSupplierResult> viewSupplierUseCase,
                              UseCase<UpdateSupplierCommand, UpdateSupplierResult> updateSupplierUseCase,
                              UseCase<DeleteSupplierCommand, DefaultResult> deleteSupplierUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createSupplierUseCase = createSupplierUseCase;
        this.viewSupplierUseCase = viewSupplierUseCase;
        this.updateSupplierUseCase = updateSupplierUseCase;
        this.deleteSupplierUseCase = deleteSupplierUseCase;
    }

    @Operation(
            summary = "Tambah supplier baru",
            description = """
                    Menambahkan supplier/pemasok baru ke dalam sistem.
                    
                    **Informasi Supplier:**
                    - `name` - Nama supplier/perusahaan
                    - `address` - Alamat lengkap supplier
                    - `phone` - Nomor telepon yang bisa dihubungi
                    - `email` - Alamat email supplier
                    - `notes` - Catatan tambahan (produk yang dijual, dll)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier berhasil ditambahkan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Sukses",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "id": "550e8400-e29b-41d4-a716-446655440030",
                                                "name": "PT Pakan Lele Sejahtera",
                                                "address": "Jl. Raya Industri No. 123, Bekasi",
                                                "phone": "021-88889999",
                                                "email": "sales@pakanlele.co.id",
                                                "notes": "Supplier utama pakan berkualitas tinggi",
                                                "createdAt": "2026-03-13T10:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data supplier baru",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Supplier Pakan",
                                    summary = "Contoh supplier pakan ikan",
                                    value = """
                                            {
                                              "name": "PT Pakan Lele Sejahtera",
                                              "address": "Jl. Raya Industri No. 123, Bekasi",
                                              "phone": "021-88889999",
                                              "email": "sales@pakanlele.co.id",
                                              "notes": "Supplier utama pakan berkualitas tinggi"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "Supplier Bibit",
                                    summary = "Contoh supplier bibit lele",
                                    value = """
                                            {
                                              "name": "CV Bibit Lele Unggul",
                                              "address": "Desa Sukamaju, Kec. Cikarang, Bekasi",
                                              "phone": "0812-3456-7890",
                                              "email": "bibit@leleunggul.com",
                                              "notes": "Spesialis bibit lele sangkuriang dan dumbo"
                                            }
                                            """
                            )
                    }
            )
    )
    @PostMapping()
    public ResponseEntity<?> createSupplier(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateSupplierRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateSupplierCommand command = new CreateSupplierCommand(userSession, request.name(), request.address(), request.phone(), request.email(), request.notes());
        CreateSupplierPresenter presenter = new CreateSupplierPresenter();
        createSupplierUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar supplier",
            description = """
                    Mengambil daftar semua supplier dalam organisasi.
                    
                    **Fitur:**
                    - Mendukung pagination
                    - Mendukung pencarian berdasarkan nama
                    - Diurutkan berdasarkan nama supplier
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Berhasil mengambil daftar supplier",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Contoh Response Daftar Supplier",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "success",
                                              "data": {
                                                "suppliers": [
                                                  {
                                                    "id": "550e8400-e29b-41d4-a716-446655440030",
                                                    "name": "CV Bibit Lele Unggul",
                                                    "address": "Desa Sukamaju, Kec. Cikarang, Bekasi",
                                                    "phone": "0812-3456-7890",
                                                    "email": "bibit@leleunggul.com",
                                                    "notes": "Spesialis bibit lele sangkuriang dan dumbo"
                                                  },
                                                  {
                                                    "id": "550e8400-e29b-41d4-a716-446655440031",
                                                    "name": "PT Pakan Lele Sejahtera",
                                                    "address": "Jl. Raya Industri No. 123, Bekasi",
                                                    "phone": "021-88889999",
                                                    "email": "sales@pakanlele.co.id",
                                                    "notes": "Supplier utama pakan berkualitas tinggi"
                                                  }
                                                ]
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewSuppliers(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "Filter berdasarkan nama supplier") @RequestParam(required = false) String name,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewSupplierQuery query = new ViewSupplierQuery(userSession, name, page, limit);
        ViewSupplierPresenter presenter = new ViewSupplierPresenter();
        viewSupplierUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update data supplier",
            description = """
                    Mengupdate informasi supplier.
                    
                    **Field yang dapat diupdate:**
                    - `name` - Nama supplier
                    - `address` - Alamat supplier
                    - `phone` - Nomor telepon
                    - `email` - Email supplier
                    - `notes` - Catatan tambahan
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Supplier tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID supplier yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateSupplierRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateSupplierCommand command = new UpdateSupplierCommand(userSession, id, request.name(), request.phone(), request.address(), request.email(), request.notes());
        UpdateSupplierPresenter presenter = new UpdateSupplierPresenter();
        updateSupplierUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus supplier",
            description = """
                    Menghapus supplier dari sistem.
                    
                    **Catatan:**
                    - Supplier yang masih memiliki kontak aktif tidak dapat dihapus
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Supplier tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID supplier yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteSupplierCommand command = new DeleteSupplierCommand(userSession, id);
        DeleteSupplierPresenter presenter = new DeleteSupplierPresenter();
        deleteSupplierUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
