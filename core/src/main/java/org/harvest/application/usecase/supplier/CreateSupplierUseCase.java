package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateSupplierCommand;
import org.harvest.application.dto.result.CreateSupplierResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SupplierRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Supplier;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreateSupplierUseCase extends AuthenticationUseCase<CreateSupplierCommand, CreateSupplierResult> {
    private final SupplierRepository supplierRepository;

    public CreateSupplierUseCase(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    protected UserSession authenticate(CreateSupplierCommand command) {
        return command.session();
    }

    @Override
    protected CreateSupplierResult executeBusiness(CreateSupplierCommand command, UserSession userSession) {
        Supplier supplier = new Supplier(supplierRepository.nextId(), command.name(), command.address(), command.phone(), command.email(), command.notes(), userSession.getOrganizationId());
        supplierRepository.save(supplier);
        return new CreateSupplierResult(supplier.id(), supplier.name(), supplier.address(), supplier.phone(), supplier.email(), supplier.notes(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateSupplierCommand command) {
        if (command.name() == null|| command.name().isBlank()){
            throw new ValidationException("Name cannot be empty");
        }
        if (command.address() == null|| command.address().isBlank()){
            throw new ValidationException("Address cannot be empty");
        }
        if (command.phone() == null|| command.phone().isBlank()){
            throw new ValidationException("Phone cannot be empty");
        }
        if (command.email() == null|| command.email().isBlank()){
            throw new ValidationException("Email cannot be empty");
        }
        if (command.notes() == null|| command.notes().isBlank()){
            throw new ValidationException("Notes cannot be empty");
        }

    }
}
