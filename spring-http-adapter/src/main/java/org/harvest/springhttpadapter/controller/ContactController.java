package org.harvest.springhttpadapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.harvest.application.dto.command.CreateContactCommand;
import org.harvest.application.dto.command.DeleteContactCommand;
import org.harvest.application.dto.command.UpdateContactCommand;
import org.harvest.application.dto.query.ViewContactQuery;
import org.harvest.application.dto.result.CreateContactResult;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdateContactResult;
import org.harvest.application.dto.result.ViewContactResult;
import org.harvest.application.port.inbound.UseCase;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.springhttpadapter.config.SessionExtractor;
import org.harvest.springhttpadapter.dto.presenter.CreateContactPresenter;
import org.harvest.springhttpadapter.dto.presenter.DeleteContactPresenter;
import org.harvest.springhttpadapter.dto.presenter.UpdateContactPresenter;
import org.harvest.springhttpadapter.dto.presenter.ViewContactPresenter;
import org.harvest.springhttpadapter.dto.request.CreateContactRequest;
import org.harvest.springhttpadapter.dto.request.UpdateContactRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/contacts")
@RestController
@Tag(name = "Contacts", description = "API untuk manajemen kontak supplier")
@SecurityRequirement(name = "bearerAuth")
public class ContactController {

    private final SessionExtractor sessionExtractor;
    private final UseCase<CreateContactCommand, CreateContactResult> createContactUseCase;
    private final UseCase<ViewContactQuery, ViewContactResult> viewContactUseCase;
    private final UseCase<UpdateContactCommand, UpdateContactResult> updateContactUseCase;
    private final UseCase<DeleteContactCommand, DefaultResult> deleteContactUseCase;

    public ContactController(SessionExtractor sessionExtractor,
                             UseCase<CreateContactCommand, CreateContactResult> createContactUseCase,
                             UseCase<ViewContactQuery, ViewContactResult> viewContactUseCase,
                             UseCase<UpdateContactCommand, UpdateContactResult> updateContactUseCase,
                             UseCase<DeleteContactCommand, DefaultResult> deleteContactUseCase) {
        this.sessionExtractor = sessionExtractor;
        this.createContactUseCase = createContactUseCase;
        this.viewContactUseCase = viewContactUseCase;
        this.updateContactUseCase = updateContactUseCase;
        this.deleteContactUseCase = deleteContactUseCase;
    }

    @Operation(
            summary = "Tambah kontak baru",
            description = """
                    Menambahkan kontak person dari supplier.
                    
                    **Informasi Kontak:**
                    - `supplierId` - UUID supplier yang memiliki kontak ini
                    - `name` - Nama kontak person
                    - `phone` - Nomor telepon
                    - `email` - Alamat email
                    - `address` - Alamat
                    - `notes` - Catatan tambahan (posisi/jabatan, dll)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kontak berhasil ditambahkan"),
            @ApiResponse(responseCode = "400", description = "Data tidak valid"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi"),
            @ApiResponse(responseCode = "404", description = "Supplier tidak ditemukan")
    })
    @PostMapping()
    public ResponseEntity<?> createContact(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @RequestBody CreateContactRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        CreateContactCommand command = new CreateContactCommand(userSession, request.supplierId(), request.name(), request.email(), request.phone(), request.address(), request.notes());
        CreateContactPresenter presenter = new CreateContactPresenter();
        createContactUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Lihat daftar kontak",
            description = """
                    Mengambil daftar kontak dari supplier tertentu.
                    
                    **Fitur:**
                    - Wajib filter berdasarkan supplier ID
                    - Mendukung pencarian berdasarkan nama
                    - Mendukung pagination
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mengambil daftar kontak"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @GetMapping()
    public ResponseEntity<?> viewContacts(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID supplier", required = true) @RequestParam UUID supplierId,
            @Parameter(description = "Filter berdasarkan nama kontak") @RequestParam(required = false) String name,
            @Parameter(description = "Nomor halaman (dimulai dari 1)", example = "1") @RequestParam(required = false, defaultValue = "1") Integer page,
            @Parameter(description = "Jumlah data per halaman", example = "10") @RequestParam(required = false, defaultValue = "10") Integer limit) {
        UserSession userSession = sessionExtractor.extract(token);
        ViewContactQuery query = new ViewContactQuery(userSession, supplierId, name, page, limit);
        ViewContactPresenter presenter = new ViewContactPresenter();
        viewContactUseCase.process(query, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Update data kontak",
            description = "Mengupdate informasi kontak supplier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kontak berhasil diupdate"),
            @ApiResponse(responseCode = "404", description = "Kontak tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kontak yang akan diupdate") @PathVariable UUID id,
            @RequestBody UpdateContactRequest request) {
        UserSession userSession = sessionExtractor.extract(token);
        UpdateContactCommand command = new UpdateContactCommand( userSession,id,  request.name(), request.address(), request.phone(), request.email(), request.notes());
        UpdateContactPresenter presenter = new UpdateContactPresenter();
        updateContactUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }

    @Operation(
            summary = "Hapus kontak",
            description = "Menghapus kontak dari sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kontak berhasil dihapus"),
            @ApiResponse(responseCode = "404", description = "Kontak tidak ditemukan"),
            @ApiResponse(responseCode = "401", description = "Tidak terautentikasi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization") String token,
            @Parameter(description = "UUID kontak yang akan dihapus") @PathVariable UUID id) {
        UserSession userSession = sessionExtractor.extract(token);
        DeleteContactCommand command = new DeleteContactCommand(userSession, id);
        DeleteContactPresenter presenter = new DeleteContactPresenter();
        deleteContactUseCase.process(command, presenter);
        return presenter.getResponseEntity();
    }
}
