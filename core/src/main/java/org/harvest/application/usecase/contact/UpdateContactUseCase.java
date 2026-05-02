package org.harvest.application.usecase.contact;

import org.harvest.application.dto.command.UpdateContactCommand;
import org.harvest.application.dto.result.UpdateContactResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;
import org.harvest.shared.exception.ValidationException;

public class UpdateContactUseCase extends AuthenticationUseCase<UpdateContactCommand, UpdateContactResult> {
    private final ContactRepository contactRepository;

    public UpdateContactUseCase(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected UserSession authenticate(UpdateContactCommand command) {
        return command.session();
    }

    @Override
    protected UpdateContactResult executeBusiness(UpdateContactCommand command, UserSession userSession) {
        Contact contact = contactRepository.findById(command.contactId());
        if (contact == null){
            throw new ValidationException("Contact not found");

        }
        Contact contactUpdate = contact.update(command.name(), command.address(), command.phone(), command.email(), command.notes());
        contactRepository.update(contactUpdate);
        return new UpdateContactResult(contactUpdate.id(), contactUpdate.name(), contactUpdate.address(), contactUpdate.phone(), contactUpdate.email(), contactUpdate.notes());
    }

    @Override
    protected void validateCommand(UpdateContactCommand command) {

        if (command.contactId() == null) {
            throw new ValidationException("Contact Id cannot be null");
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
