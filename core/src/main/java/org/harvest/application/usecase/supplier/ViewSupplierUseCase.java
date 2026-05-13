package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewSupplierQuery;
import org.harvest.application.dto.result.ViewSupplierResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.SupplierRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Supplier;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

import java.awt.print.PageFormat;
import java.util.List;

public class ViewSupplierUseCase extends AuthenticationUseCase<ViewSupplierQuery, ViewSupplierResult> {
    private final SupplierRepository supplierRepository;

    public ViewSupplierUseCase(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    protected UserSession authenticate(ViewSupplierQuery command) {
        return command.session();
    }

    @Override
    protected ViewSupplierResult executeBusiness(ViewSupplierQuery command, UserSession userSession) {
        Pagination pagination = new Pagination(command.page(),command.limit());
        List<Supplier> suppliers;
        if (command.name() == null || command.name().isBlank()) {
            suppliers = supplierRepository.findAllByOrganization(userSession.getOrganizationId(), pagination);
        } else {
            suppliers = supplierRepository.findByName(userSession.getOrganizationId(), command.name(), pagination);
        }

        return new ViewSupplierResult(suppliers);
    }

    @Override
    protected void validateCommand(ViewSupplierQuery command) {


    }
}
