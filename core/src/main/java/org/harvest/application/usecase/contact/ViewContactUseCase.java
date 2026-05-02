package org.harvest.application.usecase.contact;

import org.harvest.application.dto.query.ViewContactQuery;
import org.harvest.application.dto.result.ViewContactResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;
import org.harvest.shared.exception.ValidationException;
import org.harvest.shared.query.Pagination;

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
        Pagination pagination = new Pagination(command.page(), command.limit());
        List<Contact> contacts = contactRepository.findByFilter(command.supplierId(), command.name(), pagination);
        return new ViewContactResult(contacts);
    }

    @Override
    protected void validateCommand(ViewContactQuery command) {


        if (command.supplierId() == null) {
            throw new ValidationException("Supplier Id cannot be null");
        }

    }
}
