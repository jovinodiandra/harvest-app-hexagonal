package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewContactQuery;
import org.harvest.application.dto.result.ViewContactResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewContactUseCase extends AuthenticationUseCase<ViewContactQuery, ViewContactResult> {
    private final ContactRepository contactRepository;

    public ViewContactUseCase(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;


    }

    @Override
    protected UserSession authenticate(ViewContactQuery command) {
        return command.session();
    }

    @Override
    protected ViewContactResult executeBusiness(ViewContactQuery command, UserSession userSession) {
        int offset = (command.page() - 1) * command.limit();
        List<Contact> contacts = contactRepository.findByFilter(command.supplierId(), command.name(), offset, command.limit());
        return new ViewContactResult(contacts);
    }

    @Override
    protected void validateCommand(ViewContactQuery command) {

        if (command.page() < 1) {
            throw new ValidationException("Page must be greater than 0");
        }
        if (command.limit() < 1) {
            throw new ValidationException("Limit must be greater than 0");
        }
        if (command.supplierId() == null) {
            throw new ValidationException("Supplier Id cannot be null");
        }
        if (command.limit() > 100) {
            throw new ValidationException("Limit must be less than 100");
        }
    }
}
