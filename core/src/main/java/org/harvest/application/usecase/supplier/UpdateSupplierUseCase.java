package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateSupplierCommand;
import org.harvest.application.dto.result.UpdateSupplierResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SupplierRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Supplier;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class UpdateSupplierUseCase extends AuthenticationUseCase<UpdateSupplierCommand, UpdateSupplierResult> {
    private final SupplierRepository supplierRepository;

    public UpdateSupplierUseCase(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    protected UserSession authenticate(UpdateSupplierCommand command) {
        return command.session();
    }

    @Override
    protected UpdateSupplierResult executeBusiness(UpdateSupplierCommand command, UserSession userSession) {
        Supplier supplier = supplierRepository.findById(command.supplierId());
        if (supplier == null) {
            throw new ValidationException("Supplier not found");
        }
        Supplier updateSupplier = supplier.update(command.name(), supplier.address(), supplier.phone(), supplier.email(), supplier.notes());
        supplierRepository.update(updateSupplier);
        return new UpdateSupplierResult(updateSupplier.id(), command.name(), command.address(), command.phone(), command.email(), command.notes(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(UpdateSupplierCommand command) {

        if (command.supplierId() == null) {
            throw new ValidationException("Supplier Id cannot be null");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new ValidationException("Name cannot be empty");
        }
        if (command.address() == null || command.address().isBlank()) {
            throw new ValidationException("Address cannot be empty");
        }
        if (command.phone() == null || command.phone().isBlank()) {
            throw new ValidationException("Phone cannot be empty");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (command.notes() == null || command.notes().isBlank()) {
            throw new ValidationException("Notes cannot be empty");
        }
    }
}
