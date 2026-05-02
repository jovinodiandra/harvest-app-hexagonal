package org.harvest.application.usecase.contact;

import org.harvest.application.dto.command.DeleteContactCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;
import org.harvest.shared.exception.ValidationException;

public class DeleteContactUseCase extends AuthenticationUseCase<DeleteContactCommand, DefaultResult> {
    private final ContactRepository contactRepository;

    public DeleteContactUseCase(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected UserSession authenticate(DeleteContactCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteContactCommand command, UserSession userSession) {
        Contact contact = contactRepository.findById(command.contactId());
        if (contact == null) {
            throw new ValidationException("Contact not found");
        }

        contactRepository.delete(contact);

        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteContactCommand command) {
        if (command.contactId() == null) {

            throw new ValidationException("Contact Id cannot be null");
        }
    }
}
