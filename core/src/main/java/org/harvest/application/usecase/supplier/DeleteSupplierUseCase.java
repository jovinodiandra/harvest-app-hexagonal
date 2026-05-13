package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteSupplierCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SupplierRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Supplier;
import org.harvest.shared.exception.ValidationException;

public class DeleteSupplierUseCase extends AuthenticationUseCase<DeleteSupplierCommand, DefaultResult> {
    private final SupplierRepository supplierRepository;

    public DeleteSupplierUseCase(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;

    }

    @Override
    protected UserSession authenticate(DeleteSupplierCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteSupplierCommand command, UserSession userSession) {
        Supplier supplier = supplierRepository.findById(command.supplierId());
        if (supplier == null) {
            throw new ValidationException("Supplier not found");
        }
        supplierRepository.delete(supplier);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteSupplierCommand command) {
        if (command.supplierId() == null) {
            throw new ValidationException("Supplier Id cannot be null");
        }
    }
}
